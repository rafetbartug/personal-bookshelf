import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'

// ÖNCE Bootstrap CSS gelsin
import 'bootstrap/dist/css/bootstrap.min.css';
// (Eğer bootstrap'i npm ile yüklediysen yukarıdaki satır vardır)

// SONRA senin yazdığın CSS gelsin (Kritik nokta burası!)
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>,
)