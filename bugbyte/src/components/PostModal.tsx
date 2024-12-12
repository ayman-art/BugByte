import React, { useState } from 'react';

interface PostModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (postDetails: { title: string; content: string; community: string; tags: string[] }) => void;
}

const PostModal: React.FC<PostModalProps> = ({ isOpen, onClose, onSave }) => {
  const [tags, setTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState<string>('');
  const [selectedCommunity, setSelectedCommunity] = useState<string>('');
  const [postContent, setPostContent] = useState<string>('');
  const [postTitle, setPostTitle] = useState<string>(''); // New state for title
  
  const communities = [
    'Tech Enthusiasts',
    'Bug Hunters',
    'Developers Hub'
  ];

  const handleAddTag = () => {
    if (tagInput.trim() && !tags.includes(tagInput.trim())) {
      setTags((prevTags) => [...prevTags, tagInput.trim()]);
      setTagInput('');
    }
  };

  const handleRemoveTag = (tag: string) => {
    setTags(tags.filter((t) => t !== tag));
  };

  const handleSavePost = () => {
    onSave({
      title: postTitle, // Include title in saved post
      content: postContent,
      community: selectedCommunity,
      tags: tags
    });
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div style={styles.modal}>
      <div style={styles.modalContent}>
        <h3>Create Post</h3>
        {/* New input for post title */}
        <input
          type="text"
          style={styles.titleInput}
          placeholder="Post Title"
          value={postTitle}
          onChange={(e) => setPostTitle(e.target.value)}
        />
        <textarea 
          style={styles.textarea} 
          placeholder="Write something..." 
          value={postContent}
          onChange={(e) => setPostContent(e.target.value)}
        />
        <div style={styles.communityDropdown}>
          <label htmlFor="communitySelect">Select Community:</label>
          <select
            id="communitySelect"
            value={selectedCommunity}
            onChange={(e) => setSelectedCommunity(e.target.value)}
            style={styles.dropdown}
          >
            <option value="" disabled>
              Choose a community
            </option>
            {communities.map((community, index) => (
              <option key={index} value={community}>
                {community}
              </option>
            ))}
          </select>
        </div>
        <div style={styles.tagSection}>
          <input
            type="text"
            value={tagInput}
            onChange={(e) => setTagInput(e.target.value)}
            style={styles.tagInput}
            placeholder="Add tags..."
          />
          <button onClick={handleAddTag} style={styles.addTagButton}>
            Add Tag
          </button>
          <div style={styles.tagList}>
            {tags.map((tag, index) => (
              <div key={index} style={styles.tagItem}>
                <span>{tag}</span>
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
        <div style={styles.modalButtons}>
          <button style={styles.saveButton} onClick={handleSavePost}>
            Save Post
          </button>
          <button style={styles.closeButton} onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

const styles = {
  modal: {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1000,
  },
  modalContent: {
    backgroundColor: 'white',
    padding: '20px',
    borderRadius: '10px',
    width: '300px',
  },
  textarea: {
    width: '100%',
    height: '100px',
    marginBottom: '10px',
    padding: '10px',
    borderRadius: '5px',
    border: '1px solid #ccc',
  },
  tagSection: {
    marginBottom: '10px',
  },
  tagInput: {
    width: '70%',
    padding: '8px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    marginRight: '10px',
  },
  addTagButton: {
    backgroundColor: '#099154',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  tagList: {
    display: 'flex',
    flexWrap: 'wrap',
    marginTop: '10px',
  },
  tagItem: {
    backgroundColor: '#f1f1f1',
    padding: '5px 10px',
    margin: '5px',
    borderRadius: '5px',
    display: 'flex',
    alignItems: 'center',
  },
  removeTagButton: {
    backgroundColor: 'transparent',
    color: '#ff4757',
    border: 'none',
    marginLeft: '5px',
    cursor: 'pointer',
  },
  modalButtons: {
    display: 'flex',
    justifyContent: 'space-between',
  },
  saveButton: {
    backgroundColor: '#099154',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  closeButton: {
    backgroundColor: '#ff4757',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  communityDropdown: {
    marginBottom: '10px',
  },
  dropdown: {
    width: '100%',
    padding: '8px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    marginTop: '5px',
  },
};

export default PostModal;