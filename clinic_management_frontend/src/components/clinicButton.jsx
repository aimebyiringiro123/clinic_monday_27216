function ClinicButton({ label, onClick, variant = 'default' }) {
  return (
    <button
      className={`clinic-button ${variant}`}
      type="button"
      onClick={onClick}
    >
      {label}
    </button>
  )
}

export default ClinicButton
