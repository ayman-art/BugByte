import { useState } from "react";
import { createCommunity } from "../API/CommunityAPI";

interface CommunityDetails {
  name: string;
  description?: string;
  tags?: string[];
}

interface CommunityModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (communityDetails: CommunityDetails) => void;
  initialData?: CommunityDetails;
}

const CommunityModal: React.FC<CommunityModalProps> = ({
  isOpen,
  onClose,
  onSave,
  initialData,
}) => {
  const [communityName, setCommunityName] = useState(initialData?.name || "");
  const [description, setDescription] = useState(
    initialData?.description || ""
  );
  const [tags, setTags] = useState<string[]>(initialData?.tags || []);
  const [tagInput, setTagInput] = useState("");
  const [error, setError] = useState("");

  const handleAddTag = () => {
    if (tagInput.trim() && !tags.includes(tagInput.trim())) {
      setTags((prevTags) => [...prevTags, tagInput.trim()]);
      setTagInput("");
    }
  };

  const handleRemoveTag = (tag: string) => {
    setTags(tags.filter((t) => t !== tag));
  };

  const handleCreateCommunity = async () => {
    if (!communityName.trim()) {
      setError("Community name cannot be empty.");
      return;
    }

    const communityDetails: CommunityDetails = {
      name: communityName.trim(),
      description: description.trim(),
      tags,
    };

    try {
      const jwt = localStorage.getItem("authToken");
      const response = await createCommunity(
        communityDetails.name,
        communityDetails.description || "",
        communityDetails.tags || [],
        jwt!
      );

      alert("Community Created Successfully!");

      onSave(response);
      clearForm();
      setError("");
      onClose();
    } catch (error) {
      console.error("Error creating community:", error);
      setError("An unexpected error occurred. Please try again.");
    }
  };

  const clearForm = () => {
    setCommunityName(initialData?.name || "");
    setDescription(initialData?.description || "");
    setTags(initialData?.tags || []);
    setError("");
    setTagInput("");
  };

  const handleClose = () => {
    clearForm();
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div style={styles.modalOverlay}>
      <div style={styles.modal}>
        <h3>{initialData ? "Edit Community" : "Create Community"}</h3>
        {error && <div style={styles.errorText}>{error}</div>}
        <div style={styles.formGroup}>
          <input
            style={styles.input}
            type="text"
            placeholder="Community Name"
            value={communityName}
            onChange={(e) => setCommunityName(e.target.value)}
          />
        </div>
        <div style={styles.formGroup}>
          <textarea
            style={styles.textarea}
            placeholder="Description (Optional)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <div style={styles.tagSection}>
          <input
            style={styles.tagInput}
            type="text"
            value={tagInput}
            onChange={(e) => setTagInput(e.target.value)}
            placeholder="Add tags..."
          />
          <button style={styles.addTagButton} onClick={handleAddTag}>
            Add Tag
          </button>
          <div style={styles.tagList}>
            {tags.map((tag, index) => (
              <div key={index} style={styles.tagItem}>
                {tag}
                <button
                  style={styles.removeTagButton}
                  onClick={() => handleRemoveTag(tag)}
                >
                  X
                </button>
              </div>
            ))}
          </div>
        </div>
        <div style={styles.modalActions}>
          <button style={styles.createButton} onClick={handleCreateCommunity}>
            {initialData ? "Update" : "Create"}
          </button>
          <button style={styles.cancelButton} onClick={handleClose}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  modalOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
  },
  modal: {
    backgroundColor: "white",
    borderRadius: "10px",
    padding: "20px",
    width: "400px",
    boxShadow: "0 4px 10px rgba(0, 0, 0, 0.2)",
  },
  formGroup: {
    marginBottom: "15px",
  },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
    marginBottom: "10px",
  },
  textarea: {
    width: "100%",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
    marginBottom: "10px",
  },
  errorText: {
    color: "#f44336",
    fontSize: "12px",
    marginBottom: "10px",
  },
  tagSection: {
    marginBottom: "15px",
  },
  tagInput: {
    width: "calc(100% - 100px)",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
    marginRight: "10px",
  },
  addTagButton: {
    backgroundColor: "#4caf50",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "10px",
    cursor: "pointer",
  },
  tagList: {
    display: "flex",
    flexWrap: "wrap",
    gap: "10px",
    marginTop: "10px",
  },
  tagItem: {
    display: "flex",
    alignItems: "center",
    backgroundColor: "#e0e0e0",
    padding: "5px 10px",
    borderRadius: "5px",
  },
  removeTagButton: {
    background: "none",
    border: "none",
    marginLeft: "5px",
    color: "#f44336",
    cursor: "pointer",
  },
  modalActions: {
    display: "flex",
    justifyContent: "space-between",
  },
  createButton: {
    backgroundColor: "#4caf50",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "10px 15px",
    cursor: "pointer",
  },
  cancelButton: {
    backgroundColor: "#f44336",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "10px 15px",
    cursor: "pointer",
  },
};
export default CommunityModal;
