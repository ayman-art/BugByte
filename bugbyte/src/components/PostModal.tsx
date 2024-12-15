import React, { useState } from 'react';
import MDEditor from './MDEditor';
import '../styles/PostModal.css';

interface PostModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (postDetails: { 
    title?: string; 
    content: string; 
    community?: string; 
    tags?: string[] 
  }) => void;
  type?: 'full' | 'md-only' | 'no-community';
  communities?: string[];
}

const PostModal: React.FC<PostModalProps> = ({ 
  isOpen, 
  onClose, 
  onSave, 
  type = 'full',
  communities = ['Tech Enthusiasts', 'Bug Hunters', 'Developers Hub']
}) => {
  const [tags, setTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState<string>('');
  const [selectedCommunity, setSelectedCommunity] = useState<string>('');
  const [postContent, setPostContent] = useState<string>('');
  const [postTitle, setPostTitle] = useState<string>('');

  const handleAddTag = () => {
    if (tagInput.trim() && !tags.includes(tagInput.trim())) {
      setTags((prevTags) => [...prevTags, tagInput.trim()]);
      setTagInput('');
    }
  };

  const handleRemoveTag = (tag: string) => {
    setTags(tags.filter((t) => t !== tag));
  };

  const handleClose = () => {
    clearPost();
    onClose();
  }

  const clearPost = () => {
    setTagInput('');
    setTags([]);
    setSelectedCommunity('');
    setPostContent('');
    setPostTitle('');
  }

  const handleSavePost = () => {
    const postDetails: { 
      title?: string; 
      content: string; 
      community?: string; 
      tags?: string[] 
    } = { content: postContent };

    if (type !== 'md-only') {
      postDetails.title = postTitle;
      postDetails.tags = tags;
    }

    if (type === 'full') {
      postDetails.community = selectedCommunity;
    }

    onSave(postDetails);
    clearPost();
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal">
      <div className="modalContent">
        <h3 className="modalTitle">Create Post</h3>
        <div className="scrollableContent">
          {type !== 'md-only' && (
            <input
              type="text"
              className="titleInput"
              placeholder="Post Title"
              value={postTitle}
              onChange={(e) => setPostTitle(e.target.value)}
            />
          )}
          
          <div className="editorContainer">
            <MDEditor
              markdown={postContent}
              onChange={setPostContent}
            />
          </div>

          {type === 'full' && (
            <div className="communityDropdown">
              <label htmlFor="communitySelect">Select Community:</label>
              <select
                id="communitySelect"
                value={selectedCommunity}
                onChange={(e) => setSelectedCommunity(e.target.value)}
                className="dropdown"
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
          )}

          {type !== 'md-only' && (
            <div className="tagSection">
              <input
                type="text"
                value={tagInput}
                onChange={(e) => setTagInput(e.target.value)}
                className="tagInput"
                placeholder="Add tags..."
              />
              <button onClick={handleAddTag} className="addTagButton">
                Add Tag
              </button>
              <div className="tagList">
                {tags.map((tag, index) => (
                  <div key={index} className="tagItem">
                    <span>{tag}</span>
                    <button
                      className="removeTagButton"
                      onClick={() => handleRemoveTag(tag)}
                    >
                      X
                    </button>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
        <div className="modalButtons">
          <button className="saveButton" onClick={handleSavePost}>
            Post Question
          </button>
          <button className="closeButton" onClick={handleClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default PostModal;