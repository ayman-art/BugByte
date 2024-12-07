import React, { useState } from 'react';

import MDEditor from '../components/MDEditor';
import MDViewer from '../components/MDViewer';

const TestPage: React.FC = () => {
    const dummy: string = `
    # h1 Heading 8-)
    ## h2 Heading
    ### h3 Heading
    #### h4 Heading
    ##### h5 Heading
    ###### h6 Heading


    ## Horizontal Rules
    ![Minion](https://octodex.github.com/images/minion.png)
    `

    return (
        <div>
          <MDEditor/>
          <MDViewer markdown={dummy}/>
        </div>
      );
};

export default TestPage;