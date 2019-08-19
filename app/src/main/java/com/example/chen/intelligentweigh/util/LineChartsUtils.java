package com.example.chen.intelligentweigh.util;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author chen
 * @date 2019/8/19.   16:31
 * description：
 */
public class LineChartsUtils {

    public static void initLineCharts(String[] date,Float[] czdata,LineChartView lcv_chart,String chartDesc){
        List<PointValue> mPointValues = new ArrayList<PointValue>();
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        for(int i=0;i<date.length;i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }

        for(int i=0;i<czdata.length;i++){
            mPointValues.add(new PointValue(i,czdata[i]));
        }
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setFormatter(new SimpleLineChartValueFormatter(2));
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName(chartDesc);  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线
        data.setValueLabelTextSize(8);

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName(" ");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lcv_chart.setInteractive(true);
        lcv_chart.setZoomType(ZoomType.HORIZONTAL);
        lcv_chart.setMaxZoom((float) 2);//最大方法比例
        lcv_chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lcv_chart.setLineChartData(data);
        lcv_chart.setVisibility(View.VISIBLE);


        Viewport v = new Viewport(lcv_chart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lcv_chart.setCurrentViewport(v);
    }
}
