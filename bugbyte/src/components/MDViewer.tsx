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
    thematicBreakPlugin,
    linkDialogPlugin,
    codeBlockPlugin,
    sandpackPlugin,
    codeMirrorPlugin,
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