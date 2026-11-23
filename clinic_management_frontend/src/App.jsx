import Offices from './components/Offices.jsx'
import Doctors from './components/Doctors.jsx'
import Patients from './components/Patients.jsx'
import NavBar from './components/NavBar.jsx'
import { BrowserRouter, Routes, Route } from 'react-router'
import './App.css'

function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <NavBar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Offices />} />
            <Route path="/doctor" element={<Doctors />} />
            <Route path="/patient" element={<Patients />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  )
}

export default App
