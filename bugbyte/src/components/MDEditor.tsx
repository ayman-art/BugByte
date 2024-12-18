import React, { useState } from 'react';
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

const MDEditor: React.FC = () => {
    const [markdown, setMarkdown] = useState(`
    Title!
    ---
    content
    `);

    
    

    return (
        <div className="flex flex-col w-full max-w-5xl mx-auto p-4 space-y-4">
        <div className="border rounded-lg overflow-hidden">
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
        </div>
    );
};

export default MDEditor;