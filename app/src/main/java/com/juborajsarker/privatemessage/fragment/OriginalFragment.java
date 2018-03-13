package com.juborajsarker.privatemessage.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.juborajsarker.privatemessage.R;

import static android.content.Context.CLIPBOARD_SERVICE;


public class OriginalFragment extends Fragment {

    InterstitialAd mInterstitialAd;

    int count = 0;

    View view;



    EditText inputMsgET, inputPasscodeET, outputMsgET;
    Button returnOriginalBTN, copyBTN, shareBTN, clearBTN;

    String m="";



    public OriginalFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_original, container, false);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen1));
        init();



        return view;
    }

    private void init() {

        inputMsgET = (EditText) view.findViewById(R.id.input_secret_msg_ET);
        inputPasscodeET = (EditText) view.findViewById(R.id.input_secret_passcode_ET);
        outputMsgET = (EditText) view.findViewById(R.id.output_original_msg_ET);

        returnOriginalBTN = (Button) view.findViewById(R.id.btn_return_original);
        copyBTN = (Button) view.findViewById(R.id.btn_original_message_copy);
        shareBTN = (Button) view.findViewById(R.id.btn_original_message_share);
        clearBTN = (Button) view.findViewById(R.id.btn_clear_all_text_original);

        returnOriginalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;

                if (count % 3 == 0){

                    AdRequest adRequest = new AdRequest.Builder().addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
                    mInterstitialAd.loadAd(adRequest);

                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            showInterstitial();
                        }
                    });
                }


                returnOriginalMessage();

            }
        });

        copyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyMessage();
            }
        });

        shareBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareMessage();
            }
        });

        clearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputMsgET.setText("");
                inputPasscodeET.setText("");
                outputMsgET.setText("");
            }
        });



    }




    private void returnOriginalMessage(){



        char[] msg;
        int temp = 0;
        int pc = 0;
        int i;


        String get_msg = inputMsgET.getText().toString();

        if (inputMsgET.getText().toString().equals("")){


            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);


            outputMsgET.setText("");
            Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();


        }else {

            if (inputMsgET.getText().toString().length() <= 25){

                m = get_msg;

            }else {

                m = get_msg.substring(15, get_msg.length() - 10 );

            }


            String p = inputPasscodeET.getText().toString();

            msg = m.toCharArray();

            if (inputPasscodeET.getText().toString().equals("")) {
                pc = 200;

                for (int j = 0; j < m.length(); j++) {

                    msg[j] = (char) (msg[j] - pc);

                }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                pc = 0;
                temp = 0;



                outputMsgET.setText(String.valueOf(msg));
                outputMsgET.setVerticalScrollBarEnabled(true);
                outputMsgET.setMovementMethod(new ScrollingMovementMethod());
                outputMsgET.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);


            } else {

                for (i = 0; i < p.length(); i++) {

                    temp = p.charAt(i);
                    pc = pc + temp;
                }


                while (pc < 210) {

                    pc = pc + 50;
                }


                while (pc > 350){

                    pc = pc - 20;
                }


                for (int j = 0; j < m.length(); j++) {

                    msg[j] = (char) (msg[j] - pc);

                }

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                pc = 0;
                temp = 0;



                outputMsgET.setText(String.valueOf(msg));
                outputMsgET.setVerticalScrollBarEnabled(true);
                outputMsgET.setMovementMethod(new ScrollingMovementMethod());
                outputMsgET.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);


            }

        }










    }


    private void copyMessage(){

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);

        if (outputMsgET.getText().toString().equals("")) {

            Toast.makeText(getContext(), "Nothing to copy", Toast.LENGTH_SHORT).show();

        } else {
            String s = outputMsgET.getText().toString();
            ClipData clipe = ClipData.newPlainText("label", s);
            clipboard.setPrimaryClip(clipe);
            Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }

    }


    private void shareMessage(){

        if (outputMsgET.getText().toString().equals("")){

            Toast.makeText(getContext(), "Output message is empty", Toast.LENGTH_SHORT).show();

        }else {


            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = outputMsgET.getText().toString();
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));

        }
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
