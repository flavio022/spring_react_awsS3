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
        {userProfiles.userProfileId ? (
          <img
            src={`http://localhost:8080/api/v1/user-profile/${userProfiles.userProfileId}/image/download`}
            alt=""
          />
        ) : null}
        <br />
        <br />
        <h1>{userProfiles.userName}</h1>
        <p>{userProfiles.userProfileId}</p>
        <h1>{userProfiles.userProfileImageLink}</h1>
        <MyDropzone {...userProfiles} />
        <br />
      </div>
    );
  });
};

function MyDropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {
    // Do something with the files
    const file = acceptedFiles[0];
    console.log(userProfileId);
    const formData = new FormData();
    formData.append("file", file);

    axios
      .post(
        `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        }
      )
      .then(() => {
        console.log("file uploaded sucessfully");
      })
      .catch(err => {
        console.log(err);
      });
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
