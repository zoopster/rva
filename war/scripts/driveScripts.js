// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

var clientId = "614513768474.apps.googleusercontent.com";
//var clientId = "726689182011.apps.googleusercontent.com";

var scope = "https://www.googleapis.com/auth/drive";

var pickerApiLoaded = false;
var oauthToken;

// Use the API Loader script to load google.picker and gapi.auth.
function onApiLoad() {
	gapi.load('auth', {
		'callback' : onAuthApiLoad
	});
	gapi.load('picker', {
		'callback' : onPickerApiLoad
	});
}

function onAuthApiLoad() {
	gapi.auth.authorize({
		'client_id' : clientId,
		'scope' : scope,
		'immediate' : true
	}, handleAuthResult);
}

function onPickerApiLoad() {
	pickerApiLoaded = true;
}

function handleAuthResult(authResult) {
	var authorized = false;

	if (authResult && !authResult.error) {
		oauthToken = authResult.access_token;

		authorized = true;
	}
	
	pickerReady(authorized);
}

///**
// * Check if the current user has authorized the application.
// */
function checkAuth(viewId) {
	gapi.auth.authorize({
		'client_id' : clientId,
		'scope' : scope,
		'immediate' : false
	}, function(authResult) {
		// Access token has been successfully retrieved, requests can be sent to
		// the API.
		if (authResult && !authResult.error) {
			oauthToken = authResult.access_token;

			pickerReady(true);

			showPicker(viewId);
		} else {
			pickerCallback(null);
		}

	});
}

function getFileUrl(fileId) {
	gapi.client.load('drive', 'v2', function() {
		var request = gapi.client.drive.files.get({
			'fileId': fileId
		});
		request.execute(function(resp) {
			console.log('Title: ' + resp.webContentLink);
			
			fileUrlCallback(resp);
		});
		
	    //Make file Public on the Web by inserting new permission.
//	    var permissionBody = {
//		'value': '',
//		'type': 'anyone',
//		'role': 'reader'
//	    };
//	    
//	    var permissionRequest = gapi.client.drive.permissions.insert({
//		'fileId': self.fileID,
//		'resource': permissionBody
//	    });
//	
//	    permissionRequest.execute(function(resp) {
//		console.log(resp);
//	    });
    });
}
