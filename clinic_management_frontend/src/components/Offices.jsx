import { useEffect, useState } from 'react'
import axios from 'axios'
import ClinicButton from './clinicButton.jsx'
import '../App.css'

const API_URL = "http://localhost:8400/api/offices";

function Office() {
  const [offices, setOffices] = useState([])
  const [searchText, setSearchText] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    async function loadOffices() {
      try {
        const response = await axios.get(`${API_URL}/getAll`)
        setOffices(response.data)
      } catch {
        setError('Could not load offices. Check that the backend is running.')
      } finally {
        setLoading(false)
      }
    }

    loadOffices()
  }, [])

  async function deleteOffice(id) {
    if (!window.confirm('Are you sure you want to delete this office?'))
      return

    try {
      await axios.delete(`${API_URL}/${id}`)
      setOffices(offices.filter((office) => office.id !== id))
      setError('')
    } catch {
      setError('Could not delete this office. Please try again.')
    }
  }

  const search = searchText.trim().toLowerCase()
  const filteredOffices = offices.filter((office) =>
    office.name?.toLowerCase().includes(search) ||
    String(office.officeNumber).toLowerCase().includes(search),
  )

  return (
    <main className="offices-page">
      <header className="page-header">
        <div>
          <p className="eyebrow">CLINIC MANAGEMENT</p>
          <h1>Offices</h1>
          <p className="page-description">View and manage clinic offices.</p>
        </div>
        <span className="office-count">{offices.length} offices</span>
      </header>

      <section className="office-card" aria-label="Office list">
        <div className="list-header">
          <div>
            <h2>All offices</h2>
            <p>Search by office name or number</p>
          </div>
          <label className="search-box">
            <span className="search-icon" aria-hidden="true">⌕</span>
            <input
              type="search"
              value={searchText}
              onChange={(event) => setSearchText(event.target.value)}
              placeholder="Search offices..."
              aria-label="Search by office name or number"
            />
          </label>
        </div>

        {error && <p className="error-message" role="alert">{error}</p>}

        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Office name</th>
                <th>Office number</th>
                <th>Location</th>
                <th className="action-heading">Action</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr><td className="empty-state" colSpan="4">Loading offices...</td></tr>
              ) : filteredOffices.length === 0 ? (
                <tr>
                  <td className="empty-state" colSpan="4">
                    {search ? 'No offices match your search.' : 'No offices found.'}
                  </td>
                </tr>
              ) : filteredOffices.map((office) => (
                <tr key={office.id}>
                  <td className="office-name">{office.name}</td>
                  <td><span className="number-badge">{office.officeNumber}</span></td>
                  <td>{office.location || '—'}</td>
                  <td className="action-cell">
                    <ClinicButton
                      label="Delete"
                      variant="danger"
                      onClick={() => deleteOffice(office.id)}
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <footer className="table-footer">
          Showing {filteredOffices.length} of {offices.length} offices
        </footer>
      </section>
    </main>
  )
}

export default Office
