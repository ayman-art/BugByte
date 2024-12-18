import { SandpackConfig } from "@mdxeditor/editor"
import { API_URLS } from "../API/ApiUrls";


export default async function imageUploadHandler(image: File) {
    // THIS IS DUMMY CODE FROM THE DOCS
    const formData = new FormData()
    formData.append('file', image)
    // send the file to your server and return
    // the URL of the uploaded image in the response
    const response = await fetch(API_URLS.UPLOAD_IMAGE, {
        method: 'POST',
        body: formData
    })
    if (response.ok){
      const url = await response.text();
      return url;
    }else{
      throw new Error("File Not Found");
    }
    
}
export const languages = { codeBlockLanguages: {
    js: 'JavaScript',
    css: 'CSS',
    html: 'HTML',
    py: 'Python',
    java: 'Java',
    cpp: 'C++',
    c: 'C',
    cs: 'C#',
    ts: 'TypeScript',
    php: 'PHP',
    rb: 'Ruby',
    go: 'Go',
    rs: 'Rust',
    swift: 'Swift',
    kotlin: 'Kotlin',
    dart: 'Dart',
    r: 'R',
    lua: 'Lua',
    sql: 'SQL',
    sh: 'Shell',
    scala: 'Scala',
    vb: 'Visual Basic',
    asm: 'Assembly',
    groovy: 'Groovy',
    perl: 'Perl',
    objc: 'Objective-C',
    julia: 'Julia',
    tsx: 'TypeScript React (TSX)',
    jsx: 'JavaScript React (JSX)',
    elixir: 'Elixir',
    clj: 'Clojure',
    haskell: 'Haskell',
    fsharp: 'F#',
    erlang: 'Erlang',
    nim: 'Nim',
    crystal: 'Crystal',
    pascal: 'Pascal',
    fortran: 'Fortran',
    cobol: 'COBOL',
    vbnet: 'VB.NET'
  }
}
export const simpleSandpackConfig: SandpackConfig = {
    defaultPreset: 'react',
    presets: [
      {
        label: 'React',
        name: 'react',
        meta: 'live react',
        sandpackTemplate: 'react',
        sandpackTheme: 'dark',
        snippetFileName: '/App.js',
        snippetLanguage: 'jsx',
        initialSnippetContent: " "
      },
    ]
  }