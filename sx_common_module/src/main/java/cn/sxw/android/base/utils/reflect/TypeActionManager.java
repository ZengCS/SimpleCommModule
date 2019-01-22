package cn.sxw.android.base.utils.reflect;

import java.util.HashMap;

public class TypeActionManager {
	private static TypeActionManager sTypeActionManager = new TypeActionManager();
	private HashMap<Class<?>, TypeAction> mTypeMap = new HashMap<Class<?>, TypeAction>();
	private TypeActionManager() {
		mTypeMap.put(String.class, new StringAction());
		mTypeMap.put(int.class, new IntAction());
		mTypeMap.put(float.class, new FloatAction());
		mTypeMap.put(double.class, new DoubleAction());
		mTypeMap.put(boolean.class, new BooleanAction());
		mTypeMap.put(Integer.class, new IntAction());
		mTypeMap.put(Float.class, new FloatAction());
		mTypeMap.put(Double.class, new DoubleAction());
		mTypeMap.put(Boolean.class, new BooleanAction());
	}
	
	public static TypeActionManager getInstance() {
		return sTypeActionManager;
	}
	
	public TypeAction getTypeAction(Class<?> cls) {
		return mTypeMap.get(cls);
	}
}
