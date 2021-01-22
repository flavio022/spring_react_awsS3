import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { useDropzone } from "react-dropzone";
import "./App.css";

const UserProfile = () => {
  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.log(res);
      const data = res.data;
      setUserProfiles(data);
    });
  };

  useEffect(() => {
    fetchUserProfiles();
  }, []);
  return userProfiles.map((userProfiles, index) => {
    return (
      <div key={index}>
        <br />
        <br />
        <h1>{userProfiles.userName}</h1>
        <p>{userProfiles.userProfileId}</p>
        <h1>{userProfiles.userProfileImageLink}</h1>
        <MyDropzone />
        <br />
      </div>
    );
  });
};

function MyDropzone() {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
  }, []);
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });
  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : (
        <p>Drag 'n' drop some files here, or click to select files</p>
      )}
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <UserProfile />
    </div>
  );
}

export default App;
