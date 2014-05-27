// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.mediaLibrary;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.controller.ConfigurationController;
import com.risevision.ui.client.common.info.MediaItemInfo;
import com.risevision.ui.client.common.service.MediaLibraryService;
import com.risevision.ui.client.common.service.MediaLibraryServiceAsync;
import com.risevision.ui.client.common.utils.Base64;
import com.risevision.ui.client.common.widgets.StatusBoxWidget;

public class MediaLibraryUploadWidget extends HorizontalPanel {
    private static final String responseUrl = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/mediaLibrary/uploadComplete";
    
	private static final String FILE_NAME_PARAM = "%filename%";
	private static final String BUCKET_PARAM = "%bucket%";
	private static final String CONTENT_TYPE_PARAM = "%contenttype%";
	private static final String POLICY_STRING = "{" +
			"  \"expiration\": \"2020-01-01T12:00:00.000Z\"," +
			"  \"conditions\": [" +
			"    {\"bucket\": \"" + BUCKET_PARAM + "\" }," +
			"    {\"acl\": \"public-read\" }," +
			"    [\"eq\", \"$key\", \"" + FILE_NAME_PARAM + "\"]," +
			"    [\"starts-with\", \"$Content-Type\", \"" + CONTENT_TYPE_PARAM + "\"]," +
			"    [\"starts-with\", \"$Cache-Control\", \"public, max-age=60\"]," +
			"    {\"success_action_redirect\": \"" + responseUrl + "\" }," +
			"  ]" +
			"}";
	
	private static MediaLibraryUploadWidget instance;

	//RPC
	private MediaLibraryServiceAsync mediaLibraryService = GWT.create(MediaLibraryService.class);
	private RpcCallBackHandler rpcCallBackHandler = new RpcCallBackHandler();

	//UI pieces
	private AbsolutePanel buttonPanel = new AbsolutePanel();
	private FormPanel formPanel = new FormPanel();
	private StatusBoxWidget statusBox;
	private FileUpload fileUpload = new FileUpload();
//	private Label fileName = new Label();

	private String buttonName = "Upload";
	private Button uploadButton = new Button("<span style='white-space:nowrap;'>" + buttonName + "</span>");
	
	private Hidden keyField = new Hidden("key");
	private Hidden aclField = new Hidden("acl", CannedAccessControlList.PublicRead.toString());
	private Hidden contentTypeField = new Hidden("content-type");

	private Hidden cacheControlField = new Hidden("cache-control", "public, max-age=60");
//	private Hidden accessKeyIdField = new Hidden("AWSAccessKeyId");
	private Hidden accessKeyIdField = new Hidden("GoogleAccessId");
	
	private Hidden policyField = new Hidden("policy");
	private Hidden signatureField = new Hidden("signature");
	private Hidden actionRedirectField = new Hidden("success_action_redirect", responseUrl);

	private Frame postFrame = new Frame();
	private String postFrameName = Integer.toString((int) (Math.random() * 10000)) + "_" + new Date().getTime();
	
    private String bucketName;
	private boolean requireBucketCreation = false;
	
//	private ActionsWidget actionsWidget = new ActionsWidget();

	public MediaLibraryUploadWidget() {
		HorizontalPanel formContainerPanel = new HorizontalPanel();

		formContainerPanel.add(keyField);
		formContainerPanel.add(actionRedirectField);
		formContainerPanel.add(aclField);
		formContainerPanel.add(contentTypeField);
		formContainerPanel.add(cacheControlField);
		formContainerPanel.add(accessKeyIdField);
		formContainerPanel.add(policyField);
		formContainerPanel.add(signatureField);
		
		buttonPanel.getElement().getStyle().setMarginLeft(6, Unit.PX);

		buttonPanel.add(uploadButton);
		configureFileUploadWidget();

		formContainerPanel.add(buttonPanel);

		formPanel.add(formContainerPanel);
		
		add(formPanel);
		add(postFrame);
//		add(fileName);

//		add(actionsWidget);
//		add(new SpacerWidget());
//		add(statusBox);

		styleControls();
		initActions();	
		initHandlers();
		initForm();

		exportStaticMethods();
	}
	
	private void configureFileUploadWidget() {
		if (fileUpload != null) {
			buttonPanel.remove(fileUpload);
		}
		fileUpload = new FileUpload();
		buttonPanel.add(fileUpload, 0, 0);
		fileUpload.setName("file");
		
		fileUpload.getElement().getStyle().setOpacity(0);
		fileUpload.getElement().getStyle().setWidth(77, Unit.PX);
		
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				processForm();
				
//				fileName.setText(getFileName());
			}
		});
	}
	
	private void styleControls() {		
		postFrame.setPixelSize(0, 0);
		postFrame.setVisible(false);
				
		getElement().getStyle().setHeight(22, Unit.PX);
	}
	
	private void initActions() {
//		Command uploadCommand = new Command() {
//			@Override
//			public void execute() {
//				processForm();
//			}
//		};

//		actionsWidget.addAction("Upload", uploadCommand);

//		Command closeCommand = new Command() {
//			@Override
//			public void execute() {
////				hide();
//			}
//		};
//		
//		actionsWidget.addAction("Cancel", closeCommand);

	}
	
	private void initHandlers() {
		// Load handler for IFrame will execute either when the success_action_redirect URL (servlet) is loaded (OK)
		// or when the Amazon Response has loaded (ERROR - detected through iframe still being crossdomain)
		postFrame.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				postFrameLoad();
			}
		});
	
	}
	
	private void initForm() {
		formPanel.setAction("");

	    // Because we're going to add a FileUpload widget, we'll need to set the
	    // form to use the POST method, and multipart MIME encoding.
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.getElement().setAttribute("target", postFrameName);
		
//		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//			public void onSubmitComplete(SubmitCompleteEvent event) {
//				statusBox.setStatus(StatusBoxWidget.Status.WARNING, "File Sent");
//			}
//		});
	}
	
	private String getFileName() {
		return fileUpload.getFilename().replace("C:\\fakepath\\", "");
	}
	
	public static MediaLibraryUploadWidget getInstance() {
		try {
			if (instance == null)
				instance = new MediaLibraryUploadWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	
	
	public void setStatusBox(StatusBoxWidget statusBox) {
		this.statusBox = statusBox;
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	public void setRequireBucketCreation(boolean required) {
		requireBucketCreation = required;
	}
	
	public void show() {
//		center();
	}
	
	public void setEnabled(boolean isEnabled) {
		uploadButton.setEnabled(isEnabled);
		fileUpload.setEnabled(isEnabled);
	}
	
	private void processForm() {
		String fileNameString = getFileName();
		if (!RiseUtils.strIsNullOrEmpty(fileNameString)) {
			if (requireBucketCreation) {
				initBucket();
			}
			else {
				keyField.setValue(fileNameString);
//				accessKeyIdField.setValue(ConfigurationController.getInstance().getConfiguration().getAwsAccessKeyId());
				accessKeyIdField.setValue(ConfigurationController.getInstance().getConfiguration().getGcsAccountEmail());
				configurePolicyString(bucketName, fileNameString);
						
				updateFrameName(postFrame.getElement(), postFrameName);
			}
		}
	}
	
	private void configurePolicyString(String bucketName, String fileName) {
		String policy = POLICY_STRING.replace(FILE_NAME_PARAM, fileName);
		policy = policy.replace(BUCKET_PARAM, bucketName);
		policy = policy.replace(CONTENT_TYPE_PARAM, populateContentType(fileName));
		
		formPanel.setAction(MediaItemInfo.MEDIA_LIBRARY_URL + bucketName + "/");
		
		String policyBase64 = Base64.encode(policy);
		
		policyField.setValue(policyBase64);
		
		getSignatureString(policyBase64);
	}
	
	private String getExtension(String fileField) {
		if (fileField.indexOf('\\') > -1) {
			fileField = fileField.substring(fileField.lastIndexOf('\\') + 1, fileField.length());
		}
		if (fileField.indexOf('/') > -1) {
			fileField = fileField.substring(fileField.lastIndexOf('/') + 1, fileField.length());
		}

		String extension;
		if (fileField.indexOf('.') > -1) {
			extension = fileField.substring(fileField.lastIndexOf('.') + 1,	fileField.length());
		} else {
			extension = "";
		}
		return extension;
	}

	private String populateContentType(String fileFieldId) {
		String extension = getExtension(fileFieldId);
		String contentType = "application/octet-stream";
		if (extension.equals("txt")) {
			contentType = "text/plain";
		} else if (extension.equals("htm") || extension.equals("html")) {
			contentType = "text/html";
		} else if (extension.equals("xml")) {
			contentType = "application/xml";
		} else if (extension.equals("pdf")) {
			contentType = "application/pdf";
		} 
		
		else if (extension.equals("jpg") || extension.equals("jpeg")) {
			contentType = "image/jpeg";
		} else if (extension.equals("gif")) {
			contentType = "image/gif";
		} else if (extension.equals("png")) {
			contentType = "image/png";
		} 
		

		else if (extension.equals("wav")) {
			contentType = "audio/x-wav";
		} 
		
		else if (extension.equals("mpg") || extension.equals("mpe") || extension.equals("mpeg")) {
			contentType = "video/mpeg";
		} else if (extension.equals("ogg") || extension.equals("ogv")) {
			contentType = "video/ogg";
		} else if (extension.equals("mp4")) {
			contentType = "video/mp4";
		} else if (extension.equals("mov")) {
			contentType = "video/mov";
		} else if (extension.equals("webm")) {
			contentType = "video/webm";
		} else if (extension.equals("flv")) {
			contentType = "video/x-flv";
		} else if (extension.equals("swf")) {
			contentType = "application/x-shockwave-flash";
//		} else {
//			contentType = "content/unknown";
		}

		contentTypeField.setValue(contentType);
		return contentType;
	}
	
	private void getSignatureString(String policyBase64) {
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Retrieving Credentials...");

		mediaLibraryService.getSignedPolicy(policyBase64, rpcCallBackHandler);
	}
	
	private void configureSignatureString(String signatureString) {
		signatureField.setValue(signatureString);
		
		statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Sending File...");

		formPanel.submit();
	}
	
	private void initBucket() {
		if (requireBucketCreation) {
			statusBox.setStatus(StatusBoxWidget.Status.WARNING, "Creating Company Bucket...");
	
			mediaLibraryService.createBucket(bucketName, new RpcCreateCallBackHandler());
		}
	}
	
	class RpcCallBackHandler implements AsyncCallback<String> {
		public void onFailure(Throwable caught) {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(String result) {	
			statusBox.clear();
			
			configureSignatureString(result);
		}
	}
	
	class RpcCreateCallBackHandler implements AsyncCallback<Void> {
		public void onFailure(Throwable caught) {
			statusBox.setStatus(StatusBoxWidget.ErrorType.RPC);
		}
		
		public void onSuccess(Void result) {	
			statusBox.clear();

			requireBucketCreation = false;
			processForm();
		}
	}
	
	private static void uploadCompleteStatic(String result) {
		instance.uploadComplete(result);
	}
	
	private void uploadComplete(String result) {
		statusBox.clear();
		
		configureFileUploadWidget();
		
		MediaLibraryWidget.getInstance().load();
	}
	
	private void postFrameLoad() {
		if (!frameIsCrossDomain(postFrame.getElement())) {
			statusBox.setStatus(StatusBoxWidget.Status.ERROR, "File Upload Failed.");
		}
//		else {
//			statusBox.setStatus(StatusBoxWidget.Status.WARNING, "File Upload Successful.");
//		}
	}
	
	private static native void updateFrameName(Element myFrame, String name)  /*-{
		try {
	//		debugger; 
			myFrame.contentWindow.name = name;
		} catch (err) {}
	}-*/;
	
	private static native boolean frameIsCrossDomain(Element myFrame)  /*-{
		try {
	//		debugger; 
			if (myFrame.contentWindow.name) {
				return true;
			}
			else {
				return false;
			}
		} catch (err) {
			return false;
		}
	}-*/;
	
	public static native void exportStaticMethods() /*-{
		$wnd._uploadComplete = 
		$entry(@com.risevision.ui.client.common.widgets.mediaLibrary.MediaLibraryUploadWidget::uploadCompleteStatic(Ljava/lang/String;));
	}-*/;

}
