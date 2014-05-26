// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

function createURL(accessor, action, method, tqx, tq, oauth_consumer_key, oauth_token) {
	
	var message = { 
			action: action,
			method: method, 
			parameters: [
			             ["tqx", tqx], 
			             ["tq", tq], 
			             ["oauth_consumer_key", oauth_consumer_key],  
			             ["oauth_token", oauth_token], 
			             ["oauth_signature_method", "HMAC-SHA1"], 
			             ["oauth_timestamp", ""], 
			             ["oauth_nonce", ""], 
			             ["oauth_signature", ""] 
			            ]
           };
	
	OAuth.setTimestampAndNonce(message);
    OAuth.SignatureMethod.sign(message, accessor);
    
    return OAuth.addToURL(message.action, message.parameters);
}