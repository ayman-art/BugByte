import React, { useState } from 'react';
import MDEditor from './MDEditor';
import '../styles/PostModal.css';

interface PostDetails {
  title?: string; 
  content: string; 
  community?: string; 
  tags?: string[] 
}

interface PostModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (postDetails: PostDetails) => void;
  type?: 'full' | 'md-only' | 'no-community';
  communities?: string[];
  initialData?: PostDetails;
}

const PostModal: React.FC<PostModalProps> = ({ 
  isOpen, 
  onClose, 
  onSave, 
  type = 'full',
  communities = ['Tech Enthusiasts', 'Bug Hunters', 'Developers Hub'],
  initialData
}) => {
  const [tags, setTags] = useState<string[]>(initialData?.tags || []);
  const [tagInput, setTagInput] = useState<string>('');
  const [selectedCommunity, setSelectedCommunity] = useState<string>(initialData?.community || '');
  const [postContent, setPostContent] = useState<string>(initialData?.content || '');
  const [postTitle, setPostTitle] = useState<string>(initialData?.title || '');

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
    setTags(initialData?.tags || []);
    setSelectedCommunity(initialData?.community || '');
    setPostContent(initialData?.content || '');
    setPostTitle(initialData?.title || '');
  }

  const handleSavePost = () => {
    const postDetails: PostDetails = { content: postContent };

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
        <h3 className="modalTitle">
          {initialData ? 'Edit' : 'Create'}
        </h3>
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
            {initialData ? 'Update' : 'Post'}
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