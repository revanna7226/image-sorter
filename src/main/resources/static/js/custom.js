/*!
 * @file          /static/projects/projects.js
 * @brief         File containing event listeners to listen page events
 *				        of Project List page i.e /projects
 * @author        revannaswamy.n@quest-global.com
 * @date          13/10/2022
 * @par           Change History (Version, Date, Changed by, Changed Contents)
 *                v0.1, 13/10/2022, Revanna, Initial Creation
 * Copyright © 2022 QuEST Global, All rights reserved.
 */
var Projects = function () {

  this.SERVER_URL = window.location.protocol + '//' + window.location.host;

  this.createButtons = function(json) {
    var foo = document.getElementById("directory-button-group");
    foo.innerHTML = "";
    for (index in json) {
            let folderName = json[index];
            //Create an input type dynamically.
            var element = document.createElement("span");
            //Assign different attributes to the element.
            element.setAttribute("title", folderName);
            element.setAttribute("style", "text-align:center; margin-bottom: 2px;");
            element.setAttribute("class", "button button_panel");
            element.setAttribute("id", folderName.replace(/ /g, '_'));
            element.innerText = folderName;
            element.addEventListener("click", () => {
                var imageName = _via_img_metadata[_via_image_id].filename;
                var directoryName = folderName;
                this.moveImage(directoryName, imageName);
            });
            //Append the element in page (in span).
            foo.appendChild(element);
    }

  }

  this.registerEventForCreateDirectoryButtonClick = function() {
    document.getElementById("create-dir-button").addEventListener("click", (e) => {
        let dirName = document.getElementById("directory_name").value;
        let driveName = document.getElementById("drive-name").value;

        if(dirName != undefined && dirName != "" && driveName !== 'SELECT') {
            fetch(this.SERVER_URL + "/dirs", {
              method: "POST",
              body: JSON.stringify({
                driveName: driveName,
                dirName: dirName
              }),
              headers: {
                "Content-type": "application/json; charset=UTF-8"
              }
            })
            .then((response) => response.json())
            .then((json) => {
                console.log(json);
                this.createButtons(json);
            });
        }

    });
  }

  this.updateDriveNames = function(driveNames) {
    var select = document.getElementById('drive-name');

    for(index in driveNames) {
        var opt = document.createElement('option');
        opt.value = driveNames[index].driveLetter;
        opt.innerHTML = driveNames[index].driveName;
        select.appendChild(opt);
    }
  }

  this.registerEventForDriveSelection = () => {
    document.getElementById('drive-name').addEventListener("change", (e) => {
        const driveName = event.target.value;

        if(driveName !== 'SELECT') {
            // create default directories
            fetch(this.SERVER_URL + "/createDefaultDirs", {
              method: "POST",
              body: JSON.stringify({
                driveName: driveName
              }),
              headers: {
                "Content-type": "application/json; charset=UTF-8"
              }
            }).then(response => {
                // get all directory names and create buttons for each
                fetch(this.SERVER_URL + "/dirs/"+driveName)
                .then((response) => response.json())
                .then((json) => {
                    console.log(json);
                    this.createButtons(json);
                });
            });
        }
    });
  }

  this.moveImage = function(directoryName, imageName) {
    let driveName = document.getElementById("drive-name").value;

    fetch(this.SERVER_URL + "/move", {
      method: "POST",
      body: JSON.stringify({
          imageName: imageName,
          dirName: directoryName,
          driveName: driveName
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8"
      }
    })
    // .then((response) => response.json())
    .then(() => {
        console.log("Delete");
        this.project_file_remove();
    });
  }

  this.project_file_remove = (
      imageId = _via_image_id,
      imageIndex = _via_image_index
    ) => {
      var filename = _via_img_metadata[imageId].filename;
      var region_count = _via_img_metadata[imageId].regions.length;
      var input = {
        img_index: {
          type: "text",
          name: "File Id",
          value: imageIndex + 1,
          disabled: true,
          size: 8,
        },
        filename: {
          type: "text",
          name: "Filename",
          value: filename,
          disabled: true,
          size: 30,
        },
        region_count: {
          type: "text",
          name: "Number of regions",
          disabled: true,
          value: region_count,
          size: 8,
        },
      };
      project_file_remove_confirmed(input);
      user_input_default_cancel_handler();
    };

  this.onLoad = () => {
      // fetch all directories and update dropdown values
      fetch(this.SERVER_URL + "/drives")
      .then((response) => response.json())
      .then((json) => {
          console.log(json);
          this.updateDriveNames(json);
      });
  }

  this.process = function () {
    this.registerEventForCreateDirectoryButtonClick();
    this.onLoad();
    this.registerEventForDriveSelection();
  };
};

document.addEventListener("DOMContentLoaded", () => {
  // create Projects instance
  var instance = new Projects();
  // invoke process method
  instance.process();
});