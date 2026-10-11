FROM node:20-alpine
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
ARG VITE_API_BASE=http://localhost:3001
ENV VITE_API_BASE=$VITE_API_BASE
EXPOSE 5173
CMD ["npm","run","dev"]
