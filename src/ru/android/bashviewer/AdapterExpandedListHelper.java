package ru.android.bashviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

public class AdapterExpandedListHelper {

	final String ATTR_GROUP_NAME = "groupName";
	final String ATTR_PHONE_NAME = "attrName";

	String[] groups = new String[] { "Положительный", "Отрицательный", "Другой" };
	// названия элементов
	String[] good = new String[] { "+1", "+2", "+3", "+4", "+5"};
	String[] bad = new String[] { "-1", "-2", "-3", "-4", "-5" };
	String[] neurtal = new String[] { "0", "Сарказм", "Удалить"};

	// коллекция для групп
	ArrayList<Map<String, String>> groupData;

	// коллекция для элементов одной группы
	ArrayList<Map<String, String>> childDataItem;

	// общая коллекция для коллекций элементов
	ArrayList<ArrayList<Map<String, String>>> childData;
	// в итоге получится childData = ArrayList<childDataItem>

	// список аттрибутов группы или элемента
	Map<String, String> m;

	Context ctx;

	AdapterExpandedListHelper(Context _ctx) {
		ctx = _ctx;
	}

	SimpleExpandableListAdapter adapter;

	SimpleExpandableListAdapter getAdapter() {

		// заполняем коллекцию групп из массива с названиями групп
		groupData = new ArrayList<Map<String, String>>();
		for (String group : groups) {
			// заполняем список аттрибутов для каждой группы
			m = new HashMap<String, String>();
			m.put(ATTR_GROUP_NAME, group); // имя компании
			groupData.add(m);
		}

		// список аттрибутов групп для чтения
		String groupFrom[] = new String[] { ATTR_GROUP_NAME };
		// список ID view-элементов, в которые будет помещены аттрибуты групп
		int groupTo[] = new int[] { android.R.id.text1 };

		// создаем коллекцию для коллекций элементов
		childData = new ArrayList<ArrayList<Map<String, String>>>();

		// создаем коллекцию элементов для первой группы
		childDataItem = new ArrayList<Map<String, String>>();
		// заполняем список аттрибутов для каждого элемента
		for (String phone : good) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone); // название телефона
			childDataItem.add(m);
		}
		// добавляем в коллекцию коллекций
		childData.add(childDataItem);

		// создаем коллекцию элементов для второй группы
		childDataItem = new ArrayList<Map<String, String>>();
		for (String phone : bad) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone);
			childDataItem.add(m);
		}
		childData.add(childDataItem);

		// создаем коллекцию элементов для третьей группы
		childDataItem = new ArrayList<Map<String, String>>();
		for (String phone : neurtal) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone);
			childDataItem.add(m);
		}
		childData.add(childDataItem);

		// список аттрибутов элементов для чтения
		String childFrom[] = new String[] { ATTR_PHONE_NAME };
		// список ID view-элементов, в которые будет помещены аттрибуты
		// элементов
		int childTo[] = new int[] { android.R.id.text1 };

		adapter = new SimpleExpandableListAdapter(ctx, groupData,
				android.R.layout.simple_expandable_list_item_1, groupFrom,
				groupTo, childData, android.R.layout.simple_list_item_1,
				childFrom, childTo);

		return adapter;
	}

	String getGroupText(int groupPos) {
		return ((Map<String, String>) (adapter.getGroup(groupPos)))
				.get(ATTR_GROUP_NAME);
	}

	String getChildText(int groupPos, int childPos) {
		return ((Map<String, String>) (adapter.getChild(groupPos, childPos)))
				.get(ATTR_PHONE_NAME);
	}

	String getGroupChildText(int groupPos, int childPos) {
		return getGroupText(groupPos) + " " + getChildText(groupPos, childPos);
	}

}