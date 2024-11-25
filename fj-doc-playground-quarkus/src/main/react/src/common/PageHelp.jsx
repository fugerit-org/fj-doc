import React, { useState, useEffect } from 'react';
import Popover from '@mui/material/Popover';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import PropTypes from 'prop-types';
function PageHelp({ buttonText, helpContent }) {

  const [anchorEl, setAnchorEl] = useState(null);

  const [helpText, setHelpText] = useState(null);

    useEffect(() => {
        fetch('/fj-doc-playground/home/help/'+helpContent+'.html')
            .then((response) => response.text())
            .then((data) => setHelpText(data))
            .catch((error) => console.error('Error fetching HTML file:', error));
    }, [ helpContent ]);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? 'simple-popover' : undefined;

  return (
    <div>
      <Button aria-describedby={id} variant="contained" onClick={handleClick}>
        {buttonText}
      </Button>
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'center',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'center',
        }}
      >
          <Typography sx={{p: 2}}>
              <div dangerouslySetInnerHTML={{__html: helpText}}/>
          </Typography>
      </Popover>
    </div>
  );
}

PageHelp.propTypes = {
  buttonText: PropTypes.string.isRequired,
  helpContent: PropTypes.string.isRequired,
};

export default PageHelp;