import { SandpackConfig } from "@mdxeditor/editor"


export default async function imageUploadHandler(image: File) {
    // THIS IS DUMMY CODE FROM THE DOCS
    const formData = new FormData()
    formData.append('image', image)
    // send the file to your server and return
    // the URL of the uploaded image in the response
    const response = await fetch('/uploads/new', {
        method: 'POST',
        body: formData
    })
    const json = (await response.json()) as { url: string }
    return json.url
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