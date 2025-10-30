@echo off
echo Installing frontend dependencies and starting dev server...
cd frontend
if not exist node_modules (
  npm install
) else (
  echo node_modules exists, skipping install
)
npm run dev
