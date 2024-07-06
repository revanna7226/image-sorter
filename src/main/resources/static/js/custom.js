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
        let name = document.getElementById("directory_name").value;

        if(name != undefined && name != "") {
            fetch("http://localhost:7874/dirs", {
              method: "POST",
              body: JSON.stringify({
                name: name
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

  this.moveImage = function(directoryName, imageName) {
    fetch("http://localhost:7874/move", {
      method: "POST",
      body: JSON.stringify({
          imageName: imageName,
          dirName: directoryName
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

  this.process = function () {
    this.registerEventForCreateDirectoryButtonClick();

    fetch("http://localhost:7874/dirs")
                .then((response) => response.json())
                .then((json) => {
                    console.log(json);
                    this.createButtons(json);
                });
  };
};

document.addEventListener("DOMContentLoaded", () => {
  // create Projects instance
  var instance = new Projects();
  // invoke process method
  instance.process();
});