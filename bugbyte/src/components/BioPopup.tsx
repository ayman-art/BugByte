import React, { useState } from 'react';

// Popup component
interface PopupProps {
  initialText: string;
  onSubmit: (text: string) => void;
  onClose: () => void;
}

const TextPopup: React.FC<PopupProps> = ({ initialText, onSubmit, onClose }) => {
  const [text, setText] = useState(initialText);

  const handleSubmit = () => {
    onSubmit(text);
    onClose();
  };

  return (
    <div style={styles.overlay}>
      <div style={styles.popup}>
        <h2>Edit Bio</h2>
        <input
          type="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          style={styles.input}
        />
        <div style={styles.buttons}>
          <button onClick={handleSubmit} style={styles.submitButton}>
            Change
          </button>
          <button onClick={onClose} style={styles.cancelButton}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

const styles = {
    
    overlay: {
      position: 'fixed' as const,
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
    popup: {
      backgroundColor: 'white',
      padding: '20px',
      borderRadius: '8px',
      textAlign: 'center' as const,
      width: '300px',
    },
    input: {
      width: '100%',
      padding: '10px',
      marginBottom: '20px',
    },
    buttons: {
      display: 'flex',
      justifyContent: 'space-between',
      gap: '10px',
    },
    submitButton: {
      padding: '10px 20px',
      backgroundColor: '#4caf50',
      color: 'white',
      border: 'none',
      borderRadius: '5px',
      cursor: 'pointer',
    },
    cancelButton: {
      padding: '10px 20px',
      backgroundColor: '#f44336',
      color: 'white',
      border: 'none',
      borderRadius: '5px',
      cursor: 'pointer',
    },
  };
export default TextPopup;