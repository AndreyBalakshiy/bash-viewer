package ru.android.bashviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

public class AdapterExpandedListHelper {

	final String ATTR_GROUP_NAME = "groupName";
	final String ATTR_PHONE_NAME = "attrName";

	String[] groups = new String[] { "�������������", "�������������", "������" };
	// �������� ���������
	String[] good = new String[] { "+1", "+2", "+3", "+4", "+5"};
	String[] bad = new String[] { "-1", "-2", "-3", "-4", "-5" };
	String[] neurtal = new String[] { "0", "�������", "�������"};

	// ��������� ��� �����
	ArrayList<Map<String, String>> groupData;

	// ��������� ��� ��������� ����� ������
	ArrayList<Map<String, String>> childDataItem;

	// ����� ��������� ��� ��������� ���������
	ArrayList<ArrayList<Map<String, String>>> childData;
	// � ����� ��������� childData = ArrayList<childDataItem>

	// ������ ���������� ������ ��� ��������
	Map<String, String> m;

	Context ctx;

	AdapterExpandedListHelper(Context _ctx) {
		ctx = _ctx;
	}

	SimpleExpandableListAdapter adapter;

	SimpleExpandableListAdapter getAdapter() {

		// ��������� ��������� ����� �� ������� � ���������� �����
		groupData = new ArrayList<Map<String, String>>();
		for (String group : groups) {
			// ��������� ������ ���������� ��� ������ ������
			m = new HashMap<String, String>();
			m.put(ATTR_GROUP_NAME, group); // ��� ��������
			groupData.add(m);
		}

		// ������ ���������� ����� ��� ������
		String groupFrom[] = new String[] { ATTR_GROUP_NAME };
		// ������ ID view-���������, � ������� ����� �������� ��������� �����
		int groupTo[] = new int[] { android.R.id.text1 };

		// ������� ��������� ��� ��������� ���������
		childData = new ArrayList<ArrayList<Map<String, String>>>();

		// ������� ��������� ��������� ��� ������ ������
		childDataItem = new ArrayList<Map<String, String>>();
		// ��������� ������ ���������� ��� ������� ��������
		for (String phone : good) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone); // �������� ��������
			childDataItem.add(m);
		}
		// ��������� � ��������� ���������
		childData.add(childDataItem);

		// ������� ��������� ��������� ��� ������ ������
		childDataItem = new ArrayList<Map<String, String>>();
		for (String phone : bad) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone);
			childDataItem.add(m);
		}
		childData.add(childDataItem);

		// ������� ��������� ��������� ��� ������� ������
		childDataItem = new ArrayList<Map<String, String>>();
		for (String phone : neurtal) {
			m = new HashMap<String, String>();
			m.put(ATTR_PHONE_NAME, phone);
			childDataItem.add(m);
		}
		childData.add(childDataItem);

		// ������ ���������� ��������� ��� ������
		String childFrom[] = new String[] { ATTR_PHONE_NAME };
		// ������ ID view-���������, � ������� ����� �������� ���������
		// ���������
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