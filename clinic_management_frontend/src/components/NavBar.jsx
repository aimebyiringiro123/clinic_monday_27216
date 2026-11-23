import React from 'react'
import { Link, NavLink } from 'react-router'

function NavBar() {
  return (
    <>
      <header className="top-navbar">
        <Link className="brand" to="/">Clinic Management</Link>
        <span className="navbar-caption">Clinic Administration</span>
      </header>

    {/* =========== sidebar ================== */}
      <aside className="sidebar">
        <p className="sidebar-label">WORKSPACE</p>
        <nav className="sidebar-nav" aria-label="Main navigation">
          <NavLink to="/" end>Offices</NavLink>
          <NavLink to="/patient">Patients</NavLink>
          <NavLink to="/doctor">Doctors</NavLink>
        </nav>
      </aside>
    </>


      //  <nav>
      //      <Link to="/">Offices</Link>
      //      <Link to="/patient">Patients</Link>
      //      <Link to="/doctor">Doctors</Link>
      //  </nav>


        

        
  
   
  )
}

export default NavBar
