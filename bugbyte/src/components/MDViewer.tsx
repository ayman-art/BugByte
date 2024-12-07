import React from 'react';
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

interface MDXViewerProps {
  markdown: string;
}

const MDViewer: React.FC<MDXViewerProps> = ({ 
  markdown, 
  
}) => {
    const className = '' 
    return (
        <div className={`prose max-w-none ${className}`}>
        <MDXEditor 
            markdown={markdown} 
            readOnly 
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
                
            ]}
        />
        </div>
    );
};

export default MDViewer;