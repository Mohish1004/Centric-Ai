# AI-Powered Personal Finance & Expense Analyzer

A comprehensive, state-of-the-art full-stack command central designed to empower users to track recurring expenditures, classify income sources, establish rigid budgeting barriers, scan physical receipts via advanced OCR pipelines, and extract highly optimized Machine Learning advisories.

## 🌟 Premium System Architecture

### 1. Frontend Layer (`frontend/`)
- **Technology**: React.js 18 initialized via Vite.
- **Styling**: Tailored, rich multi-mode UI (`data-theme="dark"`) powered by Vanilla CSS. Features customized HSL variables, smooth visual gradients, glassmorphism panel containers, custom responsive matrices, and embedded CSS keyframe pulses.
- **Charts Engine**: Fully functional dynamic renders using `Chart.js` and `react-chartjs-2` supporting Side-by-side Comparative Bars, Category Dispersion Pie slices, and Multi-Month Flow trajectories.
- **Advanced UX Elements**: Includes integrated responsive sidebars, smart notification drop-downs, localized dynamic preview models for offline/demo reliability, and simple client-side image parsing logic.

### 2. Backend API Central (`backend/`)
- **Technology**: Java Spring Boot (v3.2.4) utilizing standard domain patterns.
- **Security Interceptors**: Stateless JWT filtering layer paired with custom encrypted Password authentication flows (`BCryptPasswordEncoder`).
- **Database Modularity**: Configured for instant testing convenience utilizing an Embedded H2 data instance with direct file persistence mapping. Ready for production level integration with simple configuration switches mapping directly to native `MySQL` or `PostgreSQL` connection instances.
- **Endpoints mapping**: Standardized REST configurations across Authentication APIs (`/api/auth`), Expenses layer (`/api/expenses`), Income tracking (`/api/income`), Monthly Directives (`/api/budgets`), and macro analytics grouping computations (`/api/analytics`).

### 3. Artificial Intelligence Engine (`ai-service/`)
- **Technology**: Lightweight Python FastAPI deployment executing numerical array logic.
- **Algorithms**: Uses Scikit-Learn Multi-Variate Linear Regression (`LinearRegression`) to compute forward historical trend projections and potential monthly saving offsets.
- **OCR Smart Scanners**: Contains dynamic text string extractions mapping random keyword tags directly to structured form parameters for seamless transaction logging.

---

## 🚀 Live Execution Guides

### Starting Core API Layer (Spring Boot Core)
The Spring Boot repository leverages bundled standard dependencies allowing execution directly inside compatible IDE workspaces (IntelliJ IDEA, Eclipse) or via native terminal build commands:
```bash
cd backend
mvn spring-boot:run
```
- **Service Root**: `http://localhost:8080`
- **H2 Visual Interface**: `http://localhost:8080/h2-console` (Credentials: `sa` / no password).

### Starting User Application Interface (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
- **Live Preview Target**: `http://localhost:5173`
- **Demo Identity pre-loaded**: `demo@finance.ai` / `password`

### Launching Advanced AI Machine Learning Services (Optional Engine)
```bash
cd ai-service
pip install -r requirements.txt
python main.py
```
- **Neural Endpoint Hub**: `http://localhost:8000`

---

## 🗺️ Completed Modules & Feature Checklist

| Module Number & Name | Target Implementation File Layer | Status |
| :--- | :--- | :--- |
| **1. User Authentication Module** | `backend/.../model/User.java`, `AuthController.java`, `Login.jsx` | Completed |
| **2. Income Management Module** | `IncomeService.java`, `IncomeController.java`, `Reports.jsx` | Completed |
| **3. Expense Management Module** | `ExpenseService.java`, `Expenses.jsx`, `index.css` | Completed |
| **4. Budget Planning Module** | `BudgetService.java`, `BudgetPlanner.jsx` | Completed |
| **5. Analytics Dashboard** | `AnalyticsService.java`, `Dashboard.jsx` | Completed |
| **6. AI Financial Insights Module** | `AiIntegrationService.java`, `main.py`, `Insights.jsx` | Completed |
| **7. Expense Prediction System** | `AiIntegrationService.java`, `main.py`, `Insights.jsx` | Completed |
| **8. Smart Notifications** | `Topbar.jsx`, embedded logic logs | Completed |
| **9. OCR Bill Scanner (Advanced)**| `Expenses.jsx`, `main.py`, simulated file reader | Completed |
| **10. Multi-Device Responsive UI**| `index.css`, grid mappings, theme mode variables | Completed |

---

## 🌐 Production Readiness & Going Live Protocols

### Pushing directly to your remote Git Repository
Execute these initialization sequences to configure and publish code updates immediately to native profiles:
```bash
git init
git add .
git commit -m "feat: complete full stack AI Expense Analyzer project release"
git branch -M main
git remote add origin https://github.com/your-username/finance-expense-analyzer.git
git push -u origin main
```

### Hosting Deployments (Render / Railway Target Instructions)
1. **Database Backend Deployment**: 
   - Instantiate a dedicated PostgreSQL environment directly via standard interface dashboards. Copy incoming absolute connection credentials.
   - Navigate to `backend/src/main/resources/application.properties` and paste properties overriding standard local storage options.
2. **Backend Application Layer Containerization**:
   - Route targeted build tasks inside Railway settings panels pointing base paths directly to `backend/` root contexts. Set environment parameter `PORT=8080`.
3. **Frontend Netlify/Vercel Output Deployment**:
   - Configure base path variables pointing straight to `frontend/`. Use build target command `npm run build` coupled to output dir path `dist/`.
