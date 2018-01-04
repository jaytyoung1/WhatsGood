//package com.example.android.whatsgood;
//
//import android.app.ListFragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;
//
//import java.util.ArrayList;
//
///**
// * Created by jyoun on 1/3/2018.
// */
//
//public class RestaurantListFragment extends ListFragment
//{
//    /**
//     * ArrayList of restaurants
//     */
//    ArrayList<Restaurant> restaurantsArrayList = MainActivity.restaurantsArrayList;
//
//    //public static RestaurantListFragment newInstance() { return new RestaurantListFragment(); }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
////        try
////        {
////            restaurantsArrayList = new GetRestaurantsAsyncTask(getContext()).execute().get();
////        } catch (java.lang.InterruptedException e)
////        {
////
////        } catch (java.util.concurrent.ExecutionException e)
////        {
////
////        }
//
////        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurantsArrayList, R.color.colorBackground);
////
////        setListAdapter(adapter);
////
////        return super.onCreateView(inflater, container, savedInstanceState);
//
//        View view = inflater.inflate(R.layout.restaurant_list, container, false);
//
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState)
//    {
//        super.onActivityCreated(savedInstanceState);
//
//        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), restaurantsArrayList, R.color.colorBackground);
//
//        setListAdapter(adapter);
//    }
//}
