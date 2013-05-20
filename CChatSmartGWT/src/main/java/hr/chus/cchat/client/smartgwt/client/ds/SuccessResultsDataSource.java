package hr.chus.cchat.client.smartgwt.client.ds;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.XMLTools;
import com.smartgwt.client.rpc.RPCResponse;

public class SuccessResultsDataSource extends DataSource {

    private String alternateUrl;
    
    public SuccessResultsDataSource() {
        super();
    }

    public SuccessResultsDataSource(final String p_dataUrl) {
        super(p_dataUrl);
    }

    @Override
    protected void transformResponse(final DSResponse p_response, final DSRequest p_request, final Object p_jsonData) {
        final JSONArray value = XMLTools.selectObjects(p_jsonData, "/success");
        final Boolean status = ((JSONBoolean) value.get(0)).booleanValue();
        if (!status) {
            p_response.setStatus(RPCResponse.STATUS_VALIDATION_ERROR);
            final JSONArray errors = XMLTools.selectObjects(p_jsonData, "/errorFields");
            p_response.setErrors(errors.getJavaScriptObject());
        }
    }

    @Override
    protected Object transformRequest(final DSRequest p_dsRequest) {
        p_dsRequest.setActionURL(alternateUrl);
        
        return super.transformRequest(p_dsRequest);
    }
    
    // Getters & setters

    public final String getAlternateUrl() {
        return alternateUrl;
    }

    public final void setAlternateUrl(final String p_alternateUrl) {
        alternateUrl = p_alternateUrl;
    }

}
