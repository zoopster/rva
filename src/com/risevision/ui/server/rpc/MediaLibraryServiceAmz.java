package com.risevision.ui.server.rpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.risevision.ui.client.common.exception.ServiceFailedException;
import com.risevision.ui.client.common.info.MediaItemInfo;
import com.risevision.ui.client.common.info.MediaItemsInfo;
import com.risevision.ui.client.common.service.MediaLibraryService;
import com.risevision.ui.server.amazonImpl.AWSAuthConnection;
import com.risevision.ui.server.amazonImpl.CallingFormat;
import com.risevision.ui.server.amazonImpl.ListBucketResponse;
import com.risevision.ui.server.amazonImpl.Utils;
import com.risevision.ui.server.data.DataService;
import com.risevision.ui.server.data.PersistentConfigurationInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MediaLibraryServiceAmz extends RemoteServiceServlet implements MediaLibraryService {
//public class MediaLibraryServiceAmz extends RemoteServiceServlet {
    
//    private static final String DELETED_ITEMS_KEY = "%deletedItems%";
//    private static final String MULTIPLE_DELETE_XML = "" +
//    		"    <?xml version=\"1.0\" encoding\"UTF-8\"?>" +
//    		"    <Delete>" +
//    		"        <Quiet>true</Quiet>" +
//    		"        " + DELETED_ITEMS_KEY +
//    		"    </Delete>";
//    
//    private static final String DELETED_ITEM_KEY = "%itemKey%";
//    private final String DELETED_ITEM_XML = "" +
//    		"        <Object>" +
//    		"             <Key>" + DELETED_ITEM_KEY + "</Key>" +
////    		"             <VersionId>VersionId</VersionId>" +
//    		"        </Object>";
    
    
    
    static final CallingFormat format = CallingFormat.getSubdomainCallingFormat();
    static final String location = AWSAuthConnection.LOCATION_DEFAULT;
    static final boolean secure = true;
    static final String server = Utils.DEFAULT_HOST;
    
	public MediaItemsInfo getBucketItems(String bucketName) throws ServiceFailedException {
        AWSAuthConnection conn = createAWSAuthConnection();
        
        try {
			ListBucketResponse listBucketResponse = conn.listBucket(bucketName, null, null, null, null);
			
			MediaItemsInfo mediaItemsInfo = new MediaItemsInfo();
			mediaItemsInfo.setMediaItems((ArrayList<MediaItemInfo>) listBucketResponse.entries);
			
			return mediaItemsInfo;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public void createBucket(String bucketName) throws ServiceFailedException {
        AWSAuthConnection conn = createAWSAuthConnection();
        
        try {
            conn.createBucket(bucketName, location, null);						
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
//	public void deleteMediaItem(String bucketName, String itemName) throws ServiceFailedException {
//        AWSAuthConnection conn = new AWSAuthConnection(awsAccessKeyId, awsSecretAccessKey, secure, server, format);
//        
//        try {
//        	conn.delete(bucketName, itemName, null);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void deleteMediaItems(String bucketName, ArrayList<String> itemNames) throws ServiceFailedException {
        AWSAuthConnection conn = createAWSAuthConnection();
        
//        String deletedItemsXMLDoc = ""; 
//        for (String itemName : itemNames) {
//        	deletedItemsXMLDoc += DELETED_ITEM_XML.replace(DELETED_ITEM_KEY, itemName);
//        }
//        
//        deletedItemsXMLDoc = MULTIPLE_DELETE_XML.replace(DELETED_ITEMS_KEY, deletedItemsXMLDoc);
        
        try {
        	conn.deleteItems(bucketName, itemNames, null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AWSAuthConnection createAWSAuthConnection() {
		PersistentConfigurationInfo pConfig = DataService.getInstance().getConfig();

		return new AWSAuthConnection(pConfig.getAwsAccessKeyId(), pConfig.getAwsSecretAccessKey(), secure, server, format);
	}
	
	public String getSignedPolicy(String policyBase64) {
		PersistentConfigurationInfo pConfig = DataService.getInstance().getConfig();

//		return Sha1.b64_hmac_sha1(awsSecretAccessKey, policyBase64);
		return Utils.encode(pConfig.getAwsSecretAccessKey(), policyBase64, false);
	}
}
