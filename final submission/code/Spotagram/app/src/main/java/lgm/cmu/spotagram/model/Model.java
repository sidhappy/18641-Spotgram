package lgm.cmu.spotagram.model;

import java.io.Serializable;

abstract public class Model implements Serializable {
	protected Integer id;

	public Model() {
	}

	public Model(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
