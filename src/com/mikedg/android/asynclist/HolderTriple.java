package com.mikedg.android.asynclist;


public class HolderTriple<E,H> {
	public final int position;
	public final E params;
	public final H holder;
	
	public HolderTriple(int position, E params, H holder) {
		this.params = params;
		this.holder = holder;
		this.position = position;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof HolderTriple) {
			if (this.holder == ((HolderTriple)object).holder)
			{
				return true;
			}
		} 
		
		return false;
	}   	
}