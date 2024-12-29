import React, { useState } from 'react';
import MDEditor from './MDEditor';
import '../styles/PostModal.css';
import { postQuestion } from '../API/PostAPI';
import { useNavigate } from 'react-router-dom';
import { 
  MDXEditor, 
  headingsPlugin,
  listsPlugin,
  quotePlugin,
  linkPlugin,
  imagePlugin,
  tablePlugin,
  markdownShortcutPlugin,
  toolbarPlugin,
  UndoRedo,
  BoldItalicUnderlineToggles,
  CodeToggle,
  ListsToggle,
  CreateLink,
  thematicBreakPlugin,
  InsertImage,
  InsertTable,
  Separator,
  linkDialogPlugin,
  codeBlockPlugin,
  sandpackPlugin,
  codeMirrorPlugin,
  InsertCodeBlock,
  InsertThematicBreak,
} from '@mdxeditor/editor';

import '@mdxeditor/editor/style.css';
import imageUploadHandler, { languages, simpleSandpackConfig } from '../utils/MDconfig';


interface PostDetails {
  title?: string; 
  content: string; 
  communityId?: number;
  tags?: string[] 
}

interface PostModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (postDetails: PostDetails) => void;
  type?: 'full' | 'md-only' | 'no-community';
  initialData?: PostDetails;
}

const defaultContent = `Title!
---
content`;

const PostModal: React.FC<PostModalProps> = ({ 
  isOpen, 
  onClose, 
  onSave, 
  type = 'full',
  initialData
}) => {
  const [markdown, setMarkdown] = useState(initialData?.content || defaultContent);
  const [tags, setTags] = useState<string[]>(initialData?.tags || []);
  const [tagInput, setTagInput] = useState<string>('');
  const [selectedCommunity, setSelectedCommunity] = useState<number>(initialData?.communityId!);
  const [postTitle, setPostTitle] = useState<string>(initialData?.title || '');
 const navigate = useNavigate();
  
  const joinedCommunities = JSON.parse(localStorage.getItem('joinedCommunities') || '[]');
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
    setSelectedCommunity(initialData?.communityId!);
    setMarkdown(initialData?.content || defaultContent);
    setPostTitle(initialData?.title || '');
  }

  const handleSavePost = async () => {
    const postDetails: PostDetails = { content: markdown };

    if (type !== 'md-only') {
      postDetails.title = postTitle;
      postDetails.tags = tags;
    }

    if (type === 'full') {
      postDetails.communityId = selectedCommunity;
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
            <MDXEditor
                        markdown={markdown}
                        onChange={setMarkdown}
                        plugins={[
                            headingsPlugin(),
                            listsPlugin(),
                            quotePlugin(),
                            linkPlugin(),
                            imagePlugin({imageUploadHandler}),
                            tablePlugin(),
                            codeBlockPlugin({defaultCodeBlockLanguage: 'js'}),
                            sandpackPlugin({ sandpackConfig: simpleSandpackConfig }),
                            codeMirrorPlugin(languages),
                            linkDialogPlugin(),
                            thematicBreakPlugin(),
                            markdownShortcutPlugin(),
                            toolbarPlugin({
                            toolbarContents: () => (
                                <>
                                <UndoRedo />
                                <Separator />
                                <BoldItalicUnderlineToggles />
                                <InsertThematicBreak />
                                <Separator />
                                <CodeToggle />
                                <InsertCodeBlock />
                                <Separator />
                                <ListsToggle />
                                <InsertTable />
                                <Separator />
                                <CreateLink />
                                <InsertImage />
            
                                </>
                            )
                            })
                        ]}
                        className="h-[400px]"
                        />
          </div>

          {type === 'full' && (
            <div className="communityDropdown">
            <label htmlFor="communitySelect">Select Community:</label>
            <select
              id="communitySelect"
              value={selectedCommunity}
              onChange={(e) => setSelectedCommunity(parseInt(e.target.value))}
              className="dropdown"
            >
              <option value="" disabled>
                Choose a community
              </option>
              {joinedCommunities.map((community: { id: string; name: string }, index: number) => (
                <option key={community.id} value={community.id}>
                  {community.name}
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