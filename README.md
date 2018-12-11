# RatioButton

- 带有大小单位转换功能的数量修改控件，常用于商品管理app。
- Quantity modification control with size unit conversion function, commonly used in commodity management app

## 详细介绍 ##
- 我们开发过程中，部分类型的商品管理app会经常用到大小单位转换，如酒类中的1箱=6瓶的转换。RatioButton就是将大小单位转换的逻辑，库存判断逻辑，越界判断逻辑进行了封装，避免了重复造轮子。目前的v1.0.0还是一个基础版本，后期还需要进行优化。

## 示例效果 ##
<img src="https://github.com/lintianlin/RatioButton/tree/master/gif/ratio.gif" width=300 height=450 />
<img src="https://github.com/lintianlin/RatioButton/tree/master/gif/yj.gif" width=300 height=450 />
<img src="https://github.com/lintianlin/RatioButton/tree/master/gif/zero.gif" width=300 height=450 />
<img src="https://github.com/lintianlin/RatioButton/tree/master/gif/dialog.gif" width=300 height=450 />

## 关键代码 ##

```

    public void setDisplayCount(int tempSmallCount, int tempBigCount) {
        if (ratio > 0) {//ratio为转换率，ratio>0是当有大单位的时候
            if (tempBigCount * ratio + tempSmallCount > maxCount) {
                ToastUtils.showToast(context, ERRORTIP);
                bigCount = maxCount / ratio;//取整运算
                smallCount = maxCount % ratio;//取余运算
            } else {
                if (tempSmallCount > ratio) {//当小单位数量大于ratio的时候需要进位
                    smallCount = tempSmallCount % ratio;
                    bigCount = tempBigCount + tempSmallCount / ratio;
                } else {
                    smallCount = tempSmallCount;
                    bigCount = tempBigCount;
                }
            }
        } else {
            smallCount = tempSmallCount;
            bigCount = tempBigCount;
        }
        if (smallCount == 0) {//越界判断，当数量为0时，减按钮不能点击并置灰
            btnSmallSub.setEnabled(false);
        } else {
            btnSmallSub.setEnabled(true);
        }
        if (bigCount == 0) {
            btnBigSub.setEnabled(false);
        } else {
            btnBigSub.setEnabled(true);
        }
        tvSmallCount.setText(String.valueOf(smallCount));
        tvBigCount.setText(String.valueOf(bigCount));
    }

```

## 属性 ##



| 属性 | 属性名称 | 类型 | 默认值 |
| ------ | ------ | ------ | ------ |
| btnHeight | 控件的高度 | dimension | 0 |
| btnWidth | 控件的高度 | dimension | 0 |
| tvTextSize | 数量的字体大小 | dimension | 0 |
| editable | 是否可以直接编辑 | boolean | false |
| haveBigUnit | 是否有大单位 | boolean | false |
| minAmount | 最小数量 | integer | 0 |
| margin | 小单位按钮距离大单位按钮的距离 | dimension | 10 |



## 使用方法 ##
- 1.Add it in your root build.gradle at the end of repositories:

```

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
```

- 2.Add the dependency

```

	dependencies {
		        implementation 'com.github.lintianlin:RatioButton:v1.0.0'
		}
```

- 3.edit xml

```

		<com.sinfeeloo.ratiobutton.RatiolBtn
	        android:id="@+id/bsb_goods"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_below="@+id/tv_goods_name"
	        android:layout_alignParentEnd="true"
	        app:margin="5dp"
	        app:btnHeight="30dp"
	        app:btnWidth="120dp">
		</com.sinfeeloo.ratiobutton.RatiolBtn>
```

- 4.edit java

```

			ratioBtn.setRatio(item.getRatio());
	        ratioBtn.setMaxCount(item.getStorage());
	        ratioBtn.setUnit(item.getSmallUnit(), item.getBigUnit());
	        ratioBtn.setDisplayCount(item.getSmallcount(), item.getBigCount());
	    
	        ratioBtn.setOnCountChangedLisener(new OnCountChangedListener() {
	            @Override
	            public void onCountChange(View view, int bigCount, int smallCount) {
	                
	            }
	        });
```

# License
    Copyright 2018 SinFeeLoo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.