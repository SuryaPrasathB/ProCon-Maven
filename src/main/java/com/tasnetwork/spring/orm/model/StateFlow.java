package com.tasnetwork.spring.orm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "StateFlow")

public class StateFlow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	
	@Column(columnDefinition = "INT(5)") 
	private int serialNo;
	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String path;
	@Transient
	private SimpleStringProperty pathProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String bayKey;
	@Transient
	private SimpleStringProperty bayKeyProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String executionMode; 
	@Transient
	private SimpleStringProperty executionModeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String state;
	@Transient
	private SimpleStringProperty stateProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String errorCode;
	@Transient
	private SimpleStringProperty errorCodeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String ifSuccess;
	@Transient
	private SimpleStringProperty ifSuccessProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String ifSuccessErrorCode;
	@Transient
	private SimpleStringProperty ifSuccessErrorCodeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String ifFailed;
	@Transient
	private SimpleStringProperty ifFailedProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String ifFailedErrorCode;
	@Transient
	private SimpleStringProperty ifFailedErrorCodeProperty = new SimpleStringProperty();

	

	// F U N C T I O N  D E C L A R A T I O N ===========================================================================

	public StateFlow() {

	}

	public StateFlow(
			int serialNo,
			String path,
			String bayKey,
			String executionMode,
			String state,
			String errorCode,
			String ifSuccess,
			String ifSuccessErrorCode,
			String ifFailed,
			String ifFailedErrorCode
			) {
		
		this.serialNo = serialNo;
		this.path = path;
		this.bayKey = bayKey;
		this.executionMode = executionMode;
		this.state = state;
		this.errorCode = errorCode;
		this.ifSuccess = ifSuccess;
		this.ifSuccessErrorCode = ifSuccessErrorCode;
		this.ifFailed = ifFailed;
		this.ifFailedErrorCode = ifFailedErrorCode;
		
/*		this.stateProperty.addListener((observable, oldValue, newValue) -> {
            String mappedCode = stateCodeMap.getOrDefault(newValue, "UNKNOWN_CODE");
            this.errorCodeProperty.set(mappedCode);
        });*/
	}
	
/*	 public StateFlow(String path, String state, String stateErrorCode, String success, String successErrorCode, String failed, String failedErrorCode) {
         this.path = path;
         this.state = state;
         this.errorCode = stateErrorCode;
         this.ifSuccess = success;
         this.ifSuccessErrorCode = successErrorCode;
         this.ifFailed = failed;
         this.ifFailedErrorCode = failedErrorCode;

         // Add a listener to update stateCode whenever state changes
         this.stateProperty.addListener((observable, oldValue, newValue) -> {
             String mappedCode = stateCodeMap.getOrDefault(newValue, "UNKNOWN_CODE");
             this.errorCodeProperty.set(mappedCode);
         });
     }*/
	
	@Override
    public String toString() {
        return "StateFlow{" +
                "path='" + path + '\'' +
                ", bayKey='" + bayKey + '\'' +
                ", testKey'" + executionMode + '\'' +
                ", state='" + state + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", ifSuccess='" + ifSuccess + '\'' +
                ", ifSuccessErrorCode='" + ifSuccessErrorCode + '\'' +
                ", ifFailed='" + ifFailed + '\'' +
                ", ifFailedErrorCode='" + ifFailedErrorCode + '\'' +
                '}';
    }

	// G E T T E R S  A N D  S E T T E R S
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBayKey() {
        return bayKey;
    }

    public void setBayKey(String bayKey) {
        this.bayKey = bayKey;
    }
    
    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getIfSuccess() {
        return ifSuccess;
    }

    public void setIfSuccess(String ifSuccess) {
        this.ifSuccess = ifSuccess;
    }

	public String getIfSuccessErrorCode() {
        return ifSuccessErrorCode;
    }

    public void setIfSuccessErrorCode(String ifSuccessErrorCode) {
        this.ifSuccessErrorCode = ifSuccessErrorCode;
    }

    public String getIfFailed() {
        return ifFailed;
    }

    public void setIfFailed(String ifFailed) {
        this.ifFailed = ifFailed;
    }

    public String getIfFailedErrorCode() {
        return ifFailedErrorCode;
    }

    public void setIfFailedErrorCode(String ifFailedErrorCode) {
        this.ifFailedErrorCode = ifFailedErrorCode;
    }
    
    public SimpleStringProperty getPathProperty() {
    	pathProperty.set(path);
		return pathProperty;
	}

	public void setPathProperty(SimpleStringProperty pathProperty) {
		this.pathProperty = pathProperty;
	}

	public SimpleStringProperty getBayKeyProperty() {
		return bayKeyProperty;
	}

	public void setBayKeyProperty(SimpleStringProperty bayKeyProperty) {
		this.bayKeyProperty = bayKeyProperty;
	}

	public SimpleStringProperty getStateProperty() {
		stateProperty.set(state);
		return stateProperty;
	}

	public void setStateProperty(SimpleStringProperty stateProperty) {
		this.stateProperty = stateProperty;
	}

	public SimpleStringProperty getErrorCodeProperty() {
		errorCodeProperty.set(errorCode);
		return errorCodeProperty;
	}

	public void setErrorCodeProperty(SimpleStringProperty errorCodeProperty) {
		this.errorCodeProperty = errorCodeProperty;
	}

	public SimpleStringProperty getIfSuccessProperty() {
		ifSuccessProperty.set(ifSuccess);
		return ifSuccessProperty;
	}

	public void setIfSuccessProperty(SimpleStringProperty ifSuccessProperty) {
		this.ifSuccessProperty = ifSuccessProperty;
	}

	public SimpleStringProperty getIfSuccessErrorCodeProperty() {
		ifSuccessErrorCodeProperty.set(ifSuccessErrorCode);
		return ifSuccessErrorCodeProperty;
	}

	public void setIfSuccessErrorCodeProperty(SimpleStringProperty ifSuccessErrorCodeProperty) {
		this.ifSuccessErrorCodeProperty = ifSuccessErrorCodeProperty;
	}

	public SimpleStringProperty getIfFailedProperty() {
		ifFailedProperty.set(ifFailed);
		return ifFailedProperty;
	}

	public void setIfFailedProperty(SimpleStringProperty ifFailedProperty) {
		this.ifFailedProperty = ifFailedProperty;
	}

	public SimpleStringProperty getIfFailedErrorCodeProperty() {
		ifFailedErrorCodeProperty.set(ifFailedErrorCode);
		return ifFailedErrorCodeProperty;
	}

	public void setIfFailedErrorCodeProperty(SimpleStringProperty ifFailedErrorCodeProperty) {
		this.ifFailedErrorCodeProperty = ifFailedErrorCodeProperty;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public SimpleIntegerProperty getSerialNoProperty() {
		serialNoProperty.set(serialNo);
		return serialNoProperty;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

}
