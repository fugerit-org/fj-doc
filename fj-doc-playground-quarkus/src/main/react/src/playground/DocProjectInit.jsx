import React, { useState, useEffect } from 'react';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import CircularProgress from '@mui/material/CircularProgress';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { Grid, Typography } from '@mui/material';
import Alert from '@mui/material/Alert';
import appService from '../common/app-service';

const DocProjectInit = () => {
	const [extensionList, setExtensionList] = useState([]); // State to store the list of extensions
	const [selectedExtensions, setSelectedExtensions] = useState([]); // State to store selected extensions
	const [loading, setLoading] = useState(true); // State to manage loading state
	const [groupId, setGroupId] = useState('org.fugerit.java.demo'); // State to handle groupId input
	const [artifactId, setArtifactId] = useState('fugerit-doc-demo'); // State to handle artifactId input
	const [projectVersion, setProjectVersion] = useState('1.0.0-SNAPSHOT'); // State to handle projectVersion input
	const [javaVersion, setJavaVersion] = useState('21'); // State to handle javaVersion selection
	const [venusVersion, setVenusVersion] = useState('8.7.6'); // State to handle javaVersion selection
	const [flavour, setFlavour] = useState('vanilla'); // State to handle javaVersion selection
	const [isSubmitting, setIsSubmitting] = useState(false); // State to manage submit button loading state
	const [serverMessage, setServerMessage] = useState(''); // State to store the message from the server response
	const [serverContent, setServerContent] = useState(''); // State to store the base64 encoded content from the server


	// useEffect to fetch data from the API when the component mounts
	useEffect(() => {
		// Function to fetch data from the API
		const fetchExtensions = async () => {
			try {
				appService.doAjaxMultipart('GET', '/project/extensions-list', null).then(response => {
					if (response.success) {
						const data = response.result;
						// Update the state with the fetched data
						setExtensionList(data);
					} else {
						throw new Error(`HTTP error! status: ${response.status}`);
					}
				})
			} finally {
				setSelectedExtensions( ['fj-doc-base', 'fj-doc-freemarker'] )
				setLoading(false); // Set loading to false after the data is fetched
			}
		};
		fetchExtensions();
	}, []); // Empty dependency array means this effect runs once on mount

	// Handle change event for groupId input
	const handleGroupIdChange = (event) => {
		setGroupId(event.target.value);
	};

	// Handle change event for artifactId input
	const handleArtifactIdChange = (event) => {
		setArtifactId(event.target.value);
	};

	// Handle change event for multi-select
	const handleSelectChange = (event) => {
		setSelectedExtensions(event.target.value);
	};

	// Handle change event for multi-select
	const handleProjectVersion = (event) => {
		setProjectVersion(event.target.value);
	};

	// Handle change event for javaVersion select
	const handleJavaVersionChange = (event) => {
		setJavaVersion(event.target.value);
	};

	// Handle change event for venusVersion select
	const handleVenusVersionChange = (event) => {
		setVenusVersion(event.target.value);
	};

	// Handle change event for venusVersion select
	const handleFlavourChange = (event) => {
		setFlavour(event.target.value);
	};

	// Handle the submit button click
	const handleSubmit = async () => {
		setIsSubmitting(true); // Set loading state to true when submission starts
		const requestBody = {
			groupId,
			artifactId,
			projectVersion,
			javaVersion,
			venusVersion,
			flavour,
			extensionList: selectedExtensions,
		};
		try {
			appService.doAjaxJson('POST', '/project/init', requestBody).then(response => {
				if (response.success) {
					// Handle successful response
					const result = response.result
					console.log('Response from server:', result);
					if (result.message) {
						setServerMessage(result.message);
					}
					if (result.content) {
						setServerContent(result.content);
						// Decode and download the ZIP file
						const link = document.createElement('a');
						link.href = `data:application/zip;base64,${result.content}`;
						link.download = artifactId+'.zip';
						link.click();
					}
				} else {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
			})
		} catch (error) {
			console.error('Error creating project:', error);
			alert('An error occurred while creating the project.');
		} finally {
			setIsSubmitting(false); // Reset loading state after submission is done
		}
	};

	return (
		<Box sx={{ width: 800, margin: '20px auto', padding: 2 }}>
			<Typography variant="h5" gutterBottom>Fugerit Venus Doc Project Initialization</Typography>

			<Grid container spacing={2}>
				<Grid item xs={12} sm={12}>
					{/* Text field for groupId */}
					<TextField
						label="Group ID"
						variant="outlined"
						fullWidth
						margin="normal"
						value={groupId}
						onChange={handleGroupIdChange}
					/>
				</Grid>
				<Grid item xs={12} sm={12}>
					{/* Text field for artifactId */}
					<TextField
						label="Artifact ID"
						variant="outlined"
						fullWidth
						margin="normal"
						value={artifactId}
						onChange={handleArtifactIdChange}
					/>
				</Grid>
				<Grid item xs={12} sm={6}>
					{/* Text field for projectVersion */}
					<TextField
						label="Project Version"
						variant="outlined"
						fullWidth
						margin="normal"
						value={projectVersion}
						onChange={handleProjectVersion}
					/>
				</Grid>
				<Grid item xs={12} sm={6}>
					{/* Select for javaVersion */}
					<FormControl fullWidth margin="normal">
						<InputLabel id="java-version-select-label">Java Version</InputLabel>
						<Select
							labelId="java-version-select-label"
							id="java-version-select"
							value={javaVersion}
							onChange={handleJavaVersionChange}
						>
							{/* Allowed values for javaVersion */}
							<MenuItem value={8}>8</MenuItem>
							<MenuItem value={11}>11</MenuItem>
							<MenuItem value={17}>17</MenuItem>
							<MenuItem value={21}>21</MenuItem>
						</Select>
					</FormControl>
				</Grid>
				<Grid item xs={12} sm={6}>
					{/* Select for venusVersion */}
					<FormControl fullWidth margin="normal">
						<InputLabel id="venus-version-select-label">Venus Version</InputLabel>
						<Select
							labelId="venus-version-select-label"
							id="venus-version-select"
							value={venusVersion}
							onChange={handleVenusVersionChange}
						>
							{/* Allowed values for venusVersion */}
							<MenuItem value={'8.7.6'}>8.7.6</MenuItem>
							<MenuItem value={'8.7.5'}>8.7.5</MenuItem>
							<MenuItem value={'8.7.4'}>8.7.4</MenuItem>
						</Select>
					</FormControl>
				</Grid>
				<Grid item xs={12} sm={6}>
					{/* Select for flavour */}
					<FormControl fullWidth margin="normal">
						<InputLabel id="venus-flavour-select-label">Flavour</InputLabel>
						<Select
							labelId="venus-version-select-label"
							id="venus-version-select"
							value={flavour}
							onChange={handleFlavourChange}
						>
							{/* Allowed values for flavour */}
							<MenuItem value={'vanilla'}>Vanilla</MenuItem>
							<MenuItem value={'quarkus-3'}>Quarkus 3</MenuItem>
						</Select>
					</FormControl>
				</Grid>
				<Grid item xs={12} sm={12}>
					{loading ? (
						<CircularProgress /> // Show loading spinner while data is being fetched
					) : (
						<FormControl fullWidth margin="normal">
							<InputLabel id="extension-multi-select-label">Select Extensions</InputLabel>
							<Select
								labelId="extension-multi-select-label"
								id="extension-multi-select"
								multiple
								value={selectedExtensions}
								onChange={handleSelectChange}
								renderValue={(selected) => selected.join(', ')} // Custom rendering for selected values
							>
								{extensionList.map((extension) => (
									<MenuItem key={extension} value={extension.key}>
										{extension.label}
									</MenuItem>
								))}
							</Select>
						</FormControl>
					)}
				</Grid>
			</Grid>

			{/* Button to submit the form */}
			<Button
				variant="contained"
				color="primary"
				fullWidth
				onClick={handleSubmit}
				disabled={isSubmitting} // Disable button while submitting
				sx={{ marginTop: 2 }}
			>
				{isSubmitting ? 'Submitting...' : 'Create Project'}
			</Button>













			{/* Display server message if present */}
			{serverMessage && (
				<Alert severity="info" sx={{ marginTop: 2 }}>
					{serverMessage}
				</Alert>
			)}

		</Box>
	);
};

export default DocProjectInit;
