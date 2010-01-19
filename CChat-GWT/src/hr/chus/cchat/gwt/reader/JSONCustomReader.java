package hr.chus.cchat.gwt.reader;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.data.Reader;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.util.JavaScriptObjectHelper;

public class JSONCustomReader extends Reader {
	
	static {
		init();
	}
	
	private static native void init()/*-{
		$wnd.Ext.override($wnd.Ext.data.JsonReader, {
	    getJsonAccessor: function(){
	        var re = /[\[\.]/;
	        return function(expr) {
	            try {
	                return(re.test(expr))
	                    ? new Function("rec", "defaultValue", "try{with (rec) return " + expr + " || defaultValue;}catch(e){return defaultValue;}")
	                    : function(rec, defaultValue){
	                    	try {
		                        return rec[expr] || defaultValue;
		                    } catch (e) {
		                    	return defaultValue;
		                    }
	                    };
	            } catch(e){}
	            return $wnd.Ext.emptyFn;
	        };
	    }(),
	
	    readRecords : function(o){
	
	        this.jsonData = o;
	        var s = this.meta, Record = this.recordType,
	            f = Record.prototype.fields, fi = f.items, fl = f.length;
	
	        if (!this.ef) {
	            if(s.totalProperty) {
		            this.getTotal = this.getJsonAccessor(s.totalProperty);
		        }
		        if(s.successProperty) {
		            this.getSuccess = this.getJsonAccessor(s.successProperty);
		        }
		        this.getRoot = s.root ? this.getJsonAccessor(s.root) : function(p){return p;};
		        if (s.id) {
		        	var g = this.getJsonAccessor(s.id);
		        	this.getId = function(rec) {
		        		var r = g(rec);
			        	return (r === undefined || r === "") ? null : r;
		        	};
		        } else {
		        	this.getId = function(){return null;};
		        }
	            this.ef = [];
	            for(var i = 0; i < fl; i++){
	                f = fi[i];
	                var map = (f.mapping !== undefined && f.mapping !== null) ? f.mapping : f.name;
	                this.ef[i] = this.getJsonAccessor(map);
	            }
	        }
	
	    	var root = this.getRoot(o), c = root.length, totalRecords = c, success = true;
	    	if(s.totalProperty){
	            var v = parseInt(this.getTotal(o), 10);
	            if(!isNaN(v)){
	                totalRecords = v;
	            }
	        }
	        if(s.successProperty){
	            var v = this.getSuccess(o);
	            if(v === false || v === 'false'){
	                success = false;
	            }
	        }
	        var records = [];
		    for(var i = 0; i < c; i++){
			    var n = root[i];
		        var values = {};
		        var id = this.getId(n);
		        for(var j = 0; j < fl; j++){
		            f = fi[j];
	                var v = this.ef[j](n, f.defaultValue);
	                values[f.name] = f.convert(v);
		        }
		        var record = new Record(values, id);
		        record.json = n;
		        records[i] = record;
		    }
		    return {
		        success : success,
		        records : records,
		        totalRecords : totalRecords
		    };
	    }
		})
	}-*/;
	
	/**
     * Constructs a new JsonReader.
     *
     * @param recordDef the record def
     */
    public JSONCustomReader(RecordDef recordDef) {
        setRecordDef(recordDef);
    }

    /**
     * Constructs a new JsonReader.
     *
     * @param root the root property
     * @param recordDef the record def
     */
    public JSONCustomReader(String root, RecordDef recordDef) {
        setRoot(root);
        setRecordDef(recordDef);
    }

    protected native JavaScriptObject create(JavaScriptObject config, JavaScriptObject recordDef) /*-{
        return new $wnd.Ext.data.JsonReader(config, recordDef);
    }-*/;

    //config

    /**
     * Name of the property within a row object that contains a record identifier value.
     *
     * @param id the id property
     */
    public void setId(String id) {
        JavaScriptObjectHelper.setAttribute(configJS, "id", id);
    }

    /**
     * Name of the property which contains the Array of row objects.
     *
     * @param root the root property
     */
    public void setRoot(String root) {
        JavaScriptObjectHelper.setAttribute(configJS, "root", root);
    }

    /**
     * Name of the property from which to retrieve the success attribute used by forms.
     *
     * @param successProperty the success property
     */
    public void setSuccessProperty(String successProperty) {
        JavaScriptObjectHelper.setAttribute(configJS, "successProperty", successProperty);
    }

    /**
     * Name of the property from which to retrieve the total number of records in the dataset.
     * This is only needed if the whole dataset is not passed in one go, but is being paged from the remote server.
     *
     * @param totalProperty proeprty for total number of records
     */
    public void setTotalProperty(String totalProperty) {
        JavaScriptObjectHelper.setAttribute(configJS, "totalProperty", totalProperty);
    }

}
