package com.yc.health;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TestQuestionsActivity extends KJActivity {

	@BindView(id = R.id.test_questions_backicon, click = true)
	private ImageView backIcon;
	@BindView(id = R.id.test_questions_backtext, click = true)
	private Button backText;
	@BindView(id = R.id.test_questions_home, click = true)
	private ImageView homeBtn;
	@BindView(id = R.id.test_questions_question)
	private TextView questionText;
	@BindView(id = R.id.test_questions_progress)
	private TextView progressText;
	@BindView(id = R.id.test_selected, click = true)
	private RadioGroup selectedAnswer;
	@BindView(id = R.id.test_never_item, click = true)
	private RadioButton neverBtn;
	@BindView(id = R.id.test_little_item, click = true)
	private RadioButton littleBtn;
	@BindView(id = R.id.test_sometimes_item, click = true)
	private RadioButton sometimesBtn;
	@BindView(id = R.id.test_often_item, click = true)
	private RadioButton oftenBtn;
	@BindView(id = R.id.test_always_item, click = true)
	private RadioButton alwaysBtn;
	
	private int questionCounter = 1;
	private int counter = 1; //计数当前的题目
	private String[] questions; //题目
	private ArrayList<Integer> answers = new ArrayList<Integer>(); //每道题的答案
	private float pinghe = 0; //九种体质的分数
	private float qixu = 0;
	private float yangxu = 0;
	private float yinxu = 0;
	private float tanshi = 0;
	private float shire = 0;
	private float xueyu = 0;
	private float qiyu = 0;
	private float tebing = 0;
	private String constitution = null; //体质
	
	private SharedPreferences testPreferences;
	private SharedPreferences userPreferences;
	
	private String sex = "女";
	private String who = null;//家人或者本人
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				CustomToast.showToast(aty, "已成功存储!", 6000);
			} else if ( msg.what == 2 ) {
				CustomToast.showToast(aty, "用户已成功存储!", 6000);
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.test_questions);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		Resources res =getResources();
		questions = res.getStringArray(R.array.test_questions);
		
		testPreferences = getSharedPreferences("testInfo", MODE_WORLD_READABLE);
		sex = testPreferences.getString("sex", "男");
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		
		Bundle bundle = this.getIntent().getExtras();
		who = bundle.getString("who");
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()){
		case R.id.test_questions_home:
			ActivityManager.getInstace().enterHome();
			this.showActivity(aty, HomeActivity.class);
			break;
		case R.id.test_questions_backicon:
		case R.id.test_questions_backtext:
			if ( counter == 1 ) {
				CustomToast.showToast(aty, "已经是第一题了哦！", 6000);
			} else {
				//45,46分别限男、女回答，控制跳过
				if ( counter == 46 ) {
					counter--;
				} else if ( counter == 47 ) {
					if ( "女".equals(sex) ) {
						counter--;
					} 
				}
				counter--;
				questionCounter--;
				//返回上一题的时候默认选中
				if ( counter <= answers.size() ) {
					if ( answers.get(counter-1) == 1 ) {
						neverBtn.setChecked(true);
					} else if ( answers.get(counter-1) == 2 ) {
						littleBtn.setChecked(true);
					} else if ( answers.get(counter-1) == 3 ) {
						sometimesBtn.setChecked(true);
					} else if ( answers.get(counter-1) == 4 ) {
						oftenBtn.setChecked(true);
					} else if ( answers.get(counter-1) == 5 ) {
						alwaysBtn.setChecked(true);
					}
				}
				//动画加载
				Animation outAnimation = AnimationUtils.loadAnimation(aty, R.anim.push_right_out);
				final Animation inAnimation = AnimationUtils.loadAnimation(aty, R.anim.push_left_in);
				questionText.startAnimation(outAnimation);
				outAnimation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						questionText.startAnimation(inAnimation);
						questionText.setText(questionCounter+"."+questions[counter-1]);
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationStart(Animation arg0) {
					}
				});
			} 
			break;
		case R.id.test_always_item:
		case R.id.test_often_item:
		case R.id.test_sometimes_item:
		case R.id.test_little_item:
		case R.id.test_never_item:
			if ( counter == 69 ) {
				computeConstitution();
				Editor editor = testPreferences.edit();
				editor.putString("constitution", constitution);
				editor.commit();
				
				String loginName = userPreferences.getString("loginName", null);
				String memberName = testPreferences.getString("userName", null);
				if ( loginName != null ) {
					if ( "family".equals(who) ) {
						//存储家人到数据库,并且获取家人的列表
						HttpUserRequest request = new HttpUserRequest(aty,mHandler,2);
						request.addMemberInit(loginName, memberName, constitution);
						request.start();
					} else {
						//存储自己到数据库
						HttpUserRequest request = new HttpUserRequest(aty,mHandler,5);
						request.saveUserInfoInit(loginName, memberName, sex, constitution);
						request.start();
						
						Editor userEditor = userPreferences.edit();
						userEditor.putString("userName", memberName);
						userEditor.putString("sex", sex);
						userEditor.putString("constitution", constitution);
						userEditor.commit();
					}
				} else {
					Editor userEditor = userPreferences.edit();
					userEditor.putString("userName", memberName);
					userEditor.putString("sex", sex);
					userEditor.putString("constitution", constitution);
					userEditor.commit();
				}
				
				Bundle bundle = new Bundle();
				bundle.putString("constitution", constitution);
				TestQuestionsActivity.this.showActivity(aty, TestResultActivity.class, bundle);
				this.finish();
				break;
			}
			
			if ( v.getId() == R.id.test_never_item ) {
				if ( counter <= answers.size() ) {
					answers.set(counter-1, 1);
				} else {
					answers.add(1);
					selectedAnswer.clearCheck();
				}
			} else if ( v.getId() == R.id.test_little_item ) {
				if ( counter <= answers.size() ) {
					answers.set(counter-1, 2);
				} else {
					answers.add(2);
					selectedAnswer.clearCheck();
				}
			}  else if ( v.getId() == R.id.test_sometimes_item ) {
				if ( counter <= answers.size() ) {
					answers.set(counter-1, 3);
				} else {
					answers.add(3);
					selectedAnswer.clearCheck();
				}
			}  else if ( v.getId() == R.id.test_often_item ) {
				if ( counter <= answers.size() ) {
					answers.set(counter-1, 4);
				} else {
					answers.add(4);
					selectedAnswer.clearCheck();
				}
			}  else if ( v.getId() == R.id.test_always_item ) {
				if ( counter <= answers.size() ) {
					answers.set(counter-1, 5);
				} else {
					answers.add(5);
					selectedAnswer.clearCheck();
				}
			}
			
			if ( counter == 45 ) {
				counter++;
			} else if ( counter == 44 ) {
				if ( "男".equals(sex) ) {
					counter++;
				} 
			}
			
			counter++;
			questionCounter++;
			progressText.setText("已完成（"+ answers.size() +"/68)");
			//如果下一题是已经回答过的，那么默认选择之前的选择
			if ( counter <= answers.size() ) {
				if ( answers.get(counter-1) == 1 ) {
					neverBtn.setChecked(true);
				} else if ( answers.get(counter-1) == 2 ) {
					littleBtn.setChecked(true);
				} else if ( answers.get(counter-1) == 3 ) {
					sometimesBtn.setChecked(true);
				} else if ( answers.get(counter-1) == 4 ) {
					oftenBtn.setChecked(true);
				} else if ( answers.get(counter-1) == 5 ) {
					alwaysBtn.setChecked(true);
				}
			}
			//动画加载
			Animation outAnimation = AnimationUtils.loadAnimation(aty, R.anim.push_left_out);
			final Animation inAnimation = AnimationUtils.loadAnimation(aty, R.anim.push_right_in);
			questionText.startAnimation(outAnimation);
			outAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation arg0) {
					questionText.startAnimation(inAnimation);
					questionText.setText(questionCounter+"."+questions[counter-1]);
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationStart(Animation arg0) {
				}
			});
			break;
		}
	}
	
	private void computeConstitution() {
		for ( int i = 0; i < answers.size(); i++ ) {
			if ( i >= 0 && i <= 7 ) {
				pinghe = pinghe + answers.get(i);
			}  else if ( i >= 8 && i <= 15 ) {
				qixu = qixu + answers.get(i);
			} else if ( i >= 16 && i <= 22 ) {
				yangxu = yangxu + answers.get(i);
			} else if ( i >= 23 && i <= 30 ) {
				yinxu = yinxu + answers.get(i);
			} else if ( i >= 31 && i <= 38 ) {
				tanshi = tanshi + answers.get(i);
			} else if ( i >= 39 && i <= 44 ) {
				shire = shire + answers.get(i);
			} else if ( i >= 45 && i <= 51 ) {
				xueyu = xueyu + answers.get(i);
			} else if ( i >= 52 && i <= 59 ) {
				qiyu = qiyu + answers.get(i);
			} else if ( i >= 60 && i <= 67 ) {
				tebing = tebing + answers.get(i);
			}
		}
		pinghe = ((pinghe-8)/32)*100;
		qixu = ((qixu-8)/32)*100;
		yangxu = ((yangxu-7)/28)*100;
		yinxu = ((yinxu-8)/32)*100;
		tanshi = ((tanshi-8)/32)*100;
		shire = ((shire-6)/24)*100;
		xueyu = ((xueyu-7)/28)*100;
		qiyu = ((qiyu-8)/32)*100;
		tebing = ((tebing-8)/32)*100;
		
		if ( pinghe >= 60 && qixu < 40 && yangxu < 40 && yinxu < 40 && tanshi < 40 && shire < 40
				 && xueyu < 40 && qiyu < 40 && tebing < 40 ) {
			constitution = "平和质";
		} else {
			constitution = "气虚质";
			float max = 0;
			if ( qixu >= yangxu ) {
				constitution = "气虚质";
				max = qixu;
			} else {
				constitution = "阳虚质";
				max = yangxu;
			}
			
			if ( max <= yinxu ) {
				constitution = "阴虚质";
				max = yinxu;
			} 
			
			if ( max <= tanshi ) {
				constitution = "痰湿质";
				max = tanshi;
			} 
			
			if ( max <= shire ) {
				constitution = "湿热质";
				max = shire;
			} 
			
			if ( max <= xueyu ) {
				constitution = "血瘀质";
				max = xueyu;
			} 
			
			if ( max <= qiyu ) {
				constitution = "气郁质";
				max = qiyu;
			} 
			
			if ( max <= tebing ) {
				constitution = "特禀质";
				max = tebing;
			} 
		}
	}
}
