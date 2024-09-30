package msi.crool.drawingapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context:Context,attr:AttributeSet): View(context,attr) {
    private var mDrawPath: CustomPath?=null
    private var mCanvasBitmap: Bitmap?=null
    private var mCanvasPaint: Paint?=null
    private var mDrawMap:Paint?=null
    private var mDrawPaint:Paint?=null
    private var mbrushSize:Float=0.toFloat()
    private var color=Color.GREEN
    private var canvas:Canvas?=null
    private var mPaths=ArrayList<CustomPath>()
    private var mPathUndo=ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }
    private fun setUpDrawing()
    {
        mDrawPaint=Paint()
        mDrawPath=CustomPath(color,mbrushSize)
        mDrawPaint!!.color=color
        mDrawPaint!!.style=Paint.Style.STROKE
        mDrawPaint!!.strokeJoin=Paint.Join.ROUND
        mDrawPaint!!.strokeCap=Paint.Cap.ROUND
        mCanvasPaint=Paint(Paint.DITHER_FLAG)
//        mbrushSize=20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas= Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(path in mPaths)
        {
            mDrawPaint!!.strokeWidth=path.brushThickness
            mDrawPaint!!.color=path.colors
            canvas.drawPath(path,mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty)
        {
            mDrawPaint!!.strokeWidth=mDrawPath!!.brushThickness
            mDrawPaint!!.color=mDrawPath!!.colors
            canvas.drawPath(mDrawPath!!,mDrawPaint!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX=event?.x
        val touchY=event?.y

        when(event?.action)
        {
            MotionEvent.ACTION_DOWN->{
                mDrawPath!!.colors=color
                mDrawPath!!.brushThickness=mbrushSize
                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_MOVE->{
                if(touchX!=null)
                {
                    if(touchY!=null)
                    {
                        mDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP->{
                mPaths.add(mDrawPath!!)
                mDrawPath= CustomPath(color,mbrushSize)
            }
            else-> return false
        }
        invalidate()
        return true
    }
    fun setColor(newColor: String)
    {
      color=Color.parseColor(newColor)
        mDrawPaint!!.color=color
    }
    fun undoOnClick() {
        if (mPaths.size > 0) {
            mPathUndo.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()
        }
    }

    fun redoOnClick() {
        if (mPathUndo.size > 0) {
            mPaths.add(mPathUndo.removeAt(mPathUndo.size - 1))
            invalidate()
        }
    }

    fun setSizeForBrush(newSize:Float)
    {
        mbrushSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth=mbrushSize
    }
    internal class CustomPath (var colors:Int,var brushThickness :Float):Path(){

    }
}