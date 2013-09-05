package pipesAndFilters;

/*
 * To construct my pipe interface, I consulted this code: 
 * https://code.google.com/p/pipesandfilters/source/browse/trunk/pipesandfilters/src/pipes/and/filters/?r=20
 */
public abstract class Filter {
	protected Pipe sourcePipe = null;
	protected Pipe sinkPipe = null;
	
	public abstract void pull();
	
	public void setSource(Pipe sourcePipe) {
		this.sourcePipe = sourcePipe;
	}
	
	public void setSink(Pipe sinkPipe) {
		this.sinkPipe = sinkPipe;
	}

}
