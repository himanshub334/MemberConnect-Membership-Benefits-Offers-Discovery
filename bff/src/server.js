import express from 'express';
import cors from 'cors';
import Redis from 'ioredis';

const app = express();
app.use(cors());
app.use(express.json({ limit: '1mb' }));
const port = Number(process.env.PORT || 3001);
const apiBase = process.env.SPRING_API_URL || 'http://localhost:8080';
const token = process.env.DEMO_BEARER_TOKEN || 'local-demo-token';
let redis;
try {
  redis = new Redis(process.env.REDIS_URL || 'redis://localhost:6379', { lazyConnect: true, maxRetriesPerRequest: 1 });
  redis.on('error', () => {});
  await redis.connect();
} catch { redis = null; }

app.get('/health', (_req, res) => res.json({ status: 'ok', service: 'memberconnect-bff', redis: Boolean(redis) }));

async function proxy(req, res, path) {
  const customerId = req.query.customerId || req.body?.customerId || 'customer-1';
  const cacheKey = `memberconnect:${req.method}:${path}:${customerId}`;
  if (req.method === 'GET' && redis) {
    try { const cached = await redis.get(cacheKey); if (cached) return res.type('json').send(cached); } catch {}
  }
  try {
    const url = new URL(path, apiBase);
    if (req.method === 'GET') url.searchParams.set('customerId', String(customerId));
    const response = await fetch(url, {
      method: req.method,
      headers: { 'content-type': 'application/json', 'authorization': `Bearer ${token}` },
      ...(req.method === 'POST' ? { body: JSON.stringify(req.body || {}) } : {})
    });
    const body = await response.text();
    if (response.ok && req.method === 'GET' && redis) { try { await redis.set(cacheKey, body, 'EX', 30); } catch {} }
    res.status(response.status).type(response.headers.get('content-type') || 'application/json').send(body);
  } catch { res.status(502).json({ error: 'Core service unavailable' }); }
}
app.get('/api/offers', (req,res) => proxy(req,res, '/api/offers'));
app.get('/api/offers/recommended', (req,res) => proxy(req,res, '/api/offers/recommended'));
app.get('/api/benefits', (req,res) => proxy(req,res, '/api/benefits'));
app.get('/api/benefits/:id', (req,res) => proxy(req,res, `/api/benefits/${req.params.id}`));
app.get('/api/offers/:id', (req,res) => proxy(req,res, `/api/offers/${req.params.id}`));
app.post('/api/offers/:id/redeem', (req,res) => proxy(req,res, `/api/offers/${req.params.id}/redeem`));
app.get('/api/customers/:customerId/redemptions', (req,res) => proxy(req,res, `/api/customers/${req.params.customerId}/redemptions`));
app.listen(port, () => console.log(`MemberConnect BFF listening on ${port}`));
