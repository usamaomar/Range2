package range.usamaomar.range;

/**
 * Created by usamaomar on 9/1/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by usamaomar on 7/4/17.
 */
public class RangeView extends RelativeLayout {
    public int startedMargins;
    public int endMargins;
    public int rangeWidth;
    public ArrayList<Integer> integerArrayList;
    public int minListInt;
    private Context context;
    private int width;
    private int rangeNumber;
    private int ballSize = 25;
    private int viewBetweenMargins = 2;
    private int backGroundSrc;
    private Button buttonRangeView;

    public RangeView(Context context) {
        super(context);
        this.context = context;
    }

    public RangeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        getAttrs(attributeSet);
    }

    private void setRangeNumberXml(int rangeNumber) {
        this.rangeNumber = rangeNumber;
        init();
    }

    public void setRangeNumber(int rangeNumber) {
        this.rangeNumber = rangeNumber;
        init();
    }

    public void setbackGroundSrcXml(int backGroundSrc) {
        this.backGroundSrc = backGroundSrc;
        init();
    }

    public void setbackGroundSrc(int backGroundSrc) {
        this.backGroundSrc = backGroundSrc;
        init();
    }

    private void getAttrs(AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.rangeView);
        String rangeNumber = a.getString(R.styleable.rangeView_range_number);
        String indicatorSrc = a.getString(R.styleable.rangeView_indicator_src);
        a.recycle();
        if (rangeNumber != null) {
            this.rangeNumber = Integer.parseInt(rangeNumber);
            setRangeNumberXml(Integer.parseInt(rangeNumber));
        }
        if (indicatorSrc != null) {
            this.backGroundSrc = Integer.parseInt(indicatorSrc);
            setbackGroundSrcXml(Integer.parseInt(indicatorSrc));
        }
    }

    private void init() {
        if (backGroundSrc != 0 && rangeNumber != 0) {
            getWid();
            integerArrayList = new ArrayList<>();
            View view = LayoutInflater.from(context).inflate(R.layout.activity_range_view, this);
            LinearLayout rootLinearLayout = (LinearLayout) view.findViewById(R.id.rootLinearLayout);
            LinearLayout subRootLinearLayout = (LinearLayout) view.findViewById(R.id.subRootLinearLayout);
            startedMargins = -ballSize;
            LinearLayout.LayoutParams layoutIndicator = new LinearLayout.LayoutParams((int) Util.convertDpToPixel(ballSize, context), (int) Util.convertDpToPixel(ballSize, context));
            final Button indicatorButton = new Button(context);
            final int Id = indicatorButton.getId();
            indicatorButton.setBackgroundResource(backGroundSrc);
            indicatorButton.setLayoutParams(layoutIndicator);
            //view.setMargins(startedMargins * -1, 0, endMargins* -1, 0);
            indicatorButton.setX(startedMargins);
            //
            for (int x = 0; x < rangeNumber; x++) {
                rangeWidth = (width / rangeNumber);//add range = = = = = views
                integerArrayList.add((rangeWidth * x));
            }
            integerArrayList.add(integerArrayList.size(), width);
            endMargins = width;
            //
            final DragExperimentTouchListener dragExperimentTouchListener = new DragExperimentTouchListener(indicatorButton.getX(), indicatorButton.getY(), startedMargins, endMargins);
            indicatorButton.setOnTouchListener(dragExperimentTouchListener);
            //
            for (int x = 0; x < rangeNumber; x++) {
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(integerArrayList.get(1), (int) Util.convertDpToPixel(5, context));
                Button buttonRangeView = new Button(context);
                final int buttonId = buttonRangeView.getId();
                buttonRangeView.setTextColor(Color.WHITE);
                buttonRangeView.setBackgroundColor(Color.rgb(70, 80, 90));
                buttonRangeView.setLayoutParams(layout);
                layout.setMargins(viewBetweenMargins, viewBetweenMargins, viewBetweenMargins, viewBetweenMargins);
                int val = integerArrayList.get(x) - viewBetweenMargins;
                integerArrayList.set(x, val);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) buttonRangeView.getLayoutParams();
                subRootLinearLayout.addView(buttonRangeView, layoutParams);
                buttonRangeView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int viewGetX = (int) view.getX();
                        for (int x = 0; x < integerArrayList.size(); x++) {
                            float ns = view.getX();
                            if (viewGetX < integerArrayList.get(x)) {
                                float n = view.getX();
                                if (viewGetX > minListInt) {
                                    int mud = (minListInt + integerArrayList.get(x)) / 2;
                                    if (viewGetX < mud) {
                                        dragExperimentTouchListener.goTo(indicatorButton.getY(), indicatorButton, (minListInt - (ballSize + viewBetweenMargins)));
                                        break;
                                    } else {
                                        dragExperimentTouchListener.goTo(indicatorButton.getY(), indicatorButton, (integerArrayList.get(x) - (ballSize + viewBetweenMargins)));
                                        break;
                                    }
                                }
                            } else {
                                minListInt = integerArrayList.get(x);
                            }
                        }
                    }
                });
            }
            LinearLayout.LayoutParams layoutParamsIndicator = (LinearLayout.LayoutParams) indicatorButton.getLayoutParams();
            rootLinearLayout.addView(indicatorButton, layoutParamsIndicator);
        }
    }

    //201
    private void remove(LinearLayout Layout, View view) {
        if (view != null)
            Layout.removeView(view);
    }

    private void getWid() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
    }

    public class DragExperimentTouchListener implements OnTouchListener {
        private boolean isDragging = false;
        private float lastX;
        private float lastY;
        private float deltaX;
        private int startedMargins;
        private int endMargins;

        DragExperimentTouchListener(float initalX, float initialY, int startedMargins, int endMargins) {
            lastX = initalX;
            lastY = initialY;
            this.startedMargins = startedMargins;
            this.endMargins = endMargins;
        }

        @Override
        public boolean onTouch(final View view, MotionEvent arg1) {
            int action = arg1.getAction();
            if (action == MotionEvent.ACTION_DOWN && !isDragging) {
                isDragging = true;
                deltaX = arg1.getX();
                return true;
            } else if (isDragging) {
                if (action == MotionEvent.ACTION_MOVE) {
                    if (view.getX() < startedMargins) {
                        view.setX(startedMargins);
                        view.setY(view.getY());
                        isDragging = false;
                    } else if (view.getX() > endMargins) {
                        view.setX(endMargins);
                        view.setY(view.getY());
                        isDragging = false;
                    } else {
                        isDragging = true;
                        view.setX(view.getX() + arg1.getX() - deltaX);
                        view.setY(view.getY());
                    }
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    float viewGetX = Math.round(view.getX());
                    for (int x = 0; x < integerArrayList.size(); x++) {
                        float ns = view.getX();
                        if (viewGetX < integerArrayList.get(x)) {
                            float n = view.getX();
                            if (viewGetX > minListInt) {
                                int mud = (minListInt + integerArrayList.get(x)) / 2;
                                if (viewGetX < mud) {
                                    goTo(arg1.getY(), view, (minListInt - (ballSize + viewBetweenMargins)));
                                    break;
                                } else {
                                    goTo(arg1.getY(), view, (integerArrayList.get(x) - (ballSize + viewBetweenMargins)));
                                    break;
                                }
                            }
                        } else {
                            minListInt = integerArrayList.get(x);
                        }
                    }
                    return true;
                } else if (action == MotionEvent.ACTION_CANCEL) {
                    view.setX(lastX);
                    view.setY(lastY);
                    isDragging = false;
                    return true;
                }
            }
            return false;
        }

        private void goTo(float arg1, View view, float deltaXb) {
            isDragging = false;
            lastX = deltaXb;
            lastY = arg1;
            view.setX(lastX);
        }
    }
}
