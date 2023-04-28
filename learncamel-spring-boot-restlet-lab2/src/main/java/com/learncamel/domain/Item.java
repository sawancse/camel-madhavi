package com.learncamel.domain;

import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "Item", catalog = "", schema = "shedlock_DB")
@NamedQueries({ @NamedQuery(name = "Item.findAll", query = "SELECT x FROM Item x"),
		@NamedQuery(name = "Item.findBySku", query = "SELECT x FROM Item x WHERE x.sku = 100"),})
@Embeddable
@CsvRecord(separator = ",",skipFirstLine = true)
public class Item {

	private static final long serialVersionUID = -1051349223537895941L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "itemId", nullable = false, unique = true)
	Long itemId;
	
	/*
	 * @Basic(optional = false)
	 * 
	 * @Column(name = "transactionType", nullable = false, length =
	 * 50,insert="false" update="false")
	 * 
	 * @DataField(pos = 1) private String transactionType;
	 */
	@Basic(optional = false)
	@Column(name = "txType", nullable = false, length = 50)
	 @DataField(pos = 1)
	 private String txType;
	 
	@Basic(optional = false)
	@Column(name = "sku", nullable = false, length = 50)
    @DataField(pos = 2) 
    private String sku;

	@Basic(optional = false)
	@Column(name = "itemDescription", nullable = false, length = 50)
    @DataField(pos = 3)
    private String itemDescription;

	@Basic(optional = false)
	@Column(name = "price", nullable = false, length = 50)
    @DataField(pos = 4,precision = 2)
    private BigDecimal price;

	public Item(){
		
	}

	public Item(Long itemId, String txType, String sku, String itemDescription, BigDecimal price) {
		super();
		this.itemId = itemId;
		this.txType = txType;
		this.sku = sku;
		this.itemDescription = itemDescription;
		this.price = price;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
	public String toString() {
		return "Item [txType=" + txType + ", sku=" + sku + ", itemDescription=" + itemDescription
				+ ", price=" + price + "]";
	}

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


}
