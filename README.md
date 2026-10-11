# MemberConnect — Membership Benefits & Offers Discovery

A local-first full-stack demo of a membership benefits catalogue, offer eligibility, redemption flow, a Node.js Backend-for-Frontend (BFF), responsive React UI, and a Python recommendation pipeline.

## Stack
- Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Spring Security JWT-style demo auth, MySQL
- Node.js/Express BFF, Redis cache
- React + Vite + TypeScript + CSS
- Python/pandas recommendation baseline
- OpenAPI spec, JUnit 5, Docker Compose, GitHub Actions

## Architecture
`React UI -> Node BFF -> Spring Boot API -> MySQL`
`Node BFF -> Redis (optional cache)`
`Recommendation script -> reads redemption CSV -> ranks offers`

This repository is a development demo, not a production membership or payments system. The included token handling is a lightweight local demonstration; replace it with a properly configured identity provider and validate JWT signatures/claims before production use.

## Quick start (Docker)
Requirements: Docker and Docker Compose.

```bash
docker compose up --build
```

- React: http://localhost:5173
- BFF health: http://localhost:3001/health
- Spring API health: http://localhost:8080/api/health
- OpenAPI: see `openapi/openapi.yaml`

The compose setup starts MySQL and Redis as well as the three app containers. The API seeds sample offers on startup.

## Local development
### Spring Boot
```bash
cd spring-service
mvn test
mvn spring-boot:run
```
### BFF (from the repository root)
```bash
cd bff
npm install
npm run dev
```
### Frontend (from the repository root, in a second terminal)
```bash
cd frontend
npm install
npm run dev
```
### Recommendation pipeline
```bash
cd recommendation
python -m venv .venv
# activate the venv, then:
pip install -r requirements.txt
python recommend.py --customer customer-1 --redemptions sample_redemptions.csv --offers sample_offers.csv
```

## API endpoints
- `GET /api/health`
- `GET /api/offers`
- `GET /api/offers/{id}`
- `GET /api/offers/recommended?customerId=customer-1`
- `POST /api/offers/{id}/redeem`
- `GET /api/benefits`
- `GET /api/benefits/{id}`
- `GET /api/customers/{customerId}/redemptions`
- `GET /health` (BFF)
- `GET /api/offers` (BFF proxy)
- `POST /api/offers/{id}/redeem` (BFF proxy)

## Important accuracy notes
- Recommendation output is a simple hybrid baseline using category preference and co-redemption similarity, not a trained production recommender.
- The engagement rates in the original project description (34% vs 12%) are not asserted as measured by this code. Measure them using a controlled experiment before reporting them.
- API documentation is a starter spec for the implemented demo endpoints; it should be kept in sync as the project evolves.
- JWT is represented by a development demo bearer token in the BFF. Configure real signature verification and authorization before production use.
# MemberConnect-Membership-Benefits-Offers-Discovery
