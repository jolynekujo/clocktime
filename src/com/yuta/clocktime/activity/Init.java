package com.yuta.clocktime.activity;

import java.util.ArrayList;
import java.util.List;

import com.yuta.clocktime.R;
import com.yuta.clocktime.model.District;

public class Init {
	private static final String[] cities = new String[]{
		"努库阿洛法",
		"查塔姆群岛",
		"惠灵顿",
		"金斯顿",
		"新喀里多尼亚 努美阿",
		"豪勋爵岛",
		"悉尼",
		"达尔文",
		"东京",
		"香港",
		"北京",
		"曼谷",
		"阿拉木图",
		"加德满都",
		"新德里",
		"伊斯兰堡",
		"喀布尔",
		"迪拜",
		"德黑兰",
		"利雅得",
		"伊斯坦布尔",
		"巴黎",
		"伦敦",
		"格林威治市",
		"亚速尔群岛",
		"圣约翰斯",
		"巴西利亚",
		"圣地亚哥",
		"加拉加斯",
		"纽约",
		"芝加哥",
		"丹佛",
		"西雅图",
		"安克雷奇",
		"火努鲁鲁(檀香山)",
		"中途岛",
		"埃尼维托克岛"
	};
	private static final String[] timezones = new String[]{
		"GMT+13",
		"GMT+12:45",
		"GMT+12",
		"GMT+11:30",
		"GMT+11",
		"GMT+10:30",
		"GMT+10",
		"GMT+9:30",
		"GMT+9",
		"GMT+8",
		"GMT+8",
		"GMT+7",
		"GMT+6",
		"GMT+5:45",
		"GMT+5:30",
		"GMT+5",
		"GMT+4:30",
		"GMT+4",
		"GMT+3:30",
		"GMT+3",
		"GMT+2",
		"GMT+1",
		"GMT0",
		"GMT0",
		"GMT-1",
		"GMT-3:30",
		"GMT-3",
		"GMT-4",
		"GMT-4:30",
		"GMT-5",
		"GMT-6",
		"GMT-7",
		"GMT-8",
		"GMT-9",
		"GMT-10",
		"GMT-11",
		"GMT-12",
	};
	private static final int[] flags = new int[]{
		R.drawable.tangjia,
		R.drawable.xinxilan,
		R.drawable.xinxilan,
		R.drawable.aodaliya,
		R.drawable.faguo,
		R.drawable.aodaliya,
		R.drawable.aodaliya,
		R.drawable.aodaliya,
		R.drawable.riben,
		R.drawable.zhongguo,
		R.drawable.zhongguo,
		R.drawable.taiguo,
		R.drawable.hasakesitan,
		R.drawable.niboer,
		R.drawable.yindu,
		R.drawable.bajisitan,
		R.drawable.afuhan,
		R.drawable.alianqiu,
		R.drawable.yilang,
		R.drawable.shatealabo,
		R.drawable.tuerqi,
		R.drawable.faguo,
		R.drawable.yingguo,
		R.drawable.yingguo,
		R.drawable.putaoya,
		R.drawable.jianada,
		R.drawable.baxi,
		R.drawable.zhili,
		R.drawable.weinaruila,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.meiguo,
		R.drawable.mashaoerqundao

	};
	public static List<District> InitTimeZone(){
		List<District> data = new ArrayList<District>();
		for(int i=0; i<cities.length; i++){
			District district = new District();
			district.setCity(cities[i]);
			district.setTimezone(timezones[i]);
			district.setFlag(flags[i]);
			data.add(district);
		}
		return data;
	}
}
