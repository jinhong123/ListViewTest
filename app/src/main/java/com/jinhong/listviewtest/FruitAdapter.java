package com.jinhong.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jinhong on 30/5/16.
 */

//创建一个自定义的适配器，这个适配器继承自ArrayAdapter,并将泛型指定为Fruit
// Android中提供了许多适配器的实现类，最好用的就是ArrayAdapter。它可以通过泛型来指定要适配的数据类型，然后在构造函数中把要适配的数据传入即可
public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceId;
    private Context context;
    public FruitAdapter(Context context, int textViewResourceId, List<Fruit>objects){
        //FruitAdapter重写了父类的一组构造函数，用于将上下文，ListView子项布局的id和数据都传递进来。
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    //在getView方法中，首先通过getItem()方法得到当前项的Fruit实例，然后使用LayoutInflater来为这个子项加载我们传入的布局。
    //接着调用View的findViewById()方法分别获取到ImageView和TextView的实例，并分别调用它们的setImageResource()和setText()方法来设置
    //显示的图片和文字，最后将布局返回。
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Fruit fruit = getItem(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = (TextView) view.findViewById(R.id.fruit_name);
            viewHolder.buyButton = (Button) view.findViewById(R.id.buyBtn);
            view.setTag(viewHolder);    //将ViewHolder存储在view中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();  //重新获取ViewHolder
        }
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());

        viewHolder.position = position;
        viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getItem(viewHolder.position).getName() + "has been bought", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    class ViewHolder{
        ImageView fruitImage;
        TextView fruitName;
        Button buyButton;
        int position;
    }
    //ListView的很多的细节可以优化，其中运行效率就是很重要的一点。
    //因为在FruitAdapter的getView()每次都会将布局重新加载了一遍，当ListView快速滚动的时候就会成为性能的瓶颈。
    //getView()有一个convertView参数，这个参数用于将之前加载好的布局进行缓存，以便之后可以重新重用。
    //getView()进行了判断，如果convertView为空，则使用LayoutInflater去加载布局，如果不为空则直接对convertView进行重用。这样就大大提高了ListView的运行效率。在快速滚动的时候也可以表现出更好的性能。
    //虽然现在已经不会再重复去加载布局了，但是每次在getView方法还是会调用View的findViewById()来获取一次控件的实例
    //我们可以新增一个内部类ViewHolder,用于对控件的实例进行缓存，当convertView为空的时候，创建一个ViewHolder对象，并将控件的实例都存放在ViewHolder里，然后调用View的setTag()方法，将ViewHolder对象存储在View中。
    //当convertView不空的时候则调用View的getTag方法，把ViewHolder重新取出。这样所有的控件的实例都缓存在了ViewHolder里，就没有必要每次都通过findViewById()方法来获取控件实例了。
}



