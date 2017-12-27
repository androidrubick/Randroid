package androidrubick.android.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidrubick.android.app.ARContext;
import androidrubick.base.collection.CollectionsCompat;

/**
 * {@link SharedPreferences.Editor#commit()} is sync;
 * {@link SharedPreferences.Editor#apply()} is async;
 *
 * but, it maybe block anytime when activity pause or stop;
 *
 * even cause ANR!
 *
 * so we do commit at low priority, in sub threads;
 *
 * visit values from {@link Bundle memory cache} preferentially
 *
 * <p>
 * Created by Yin Yong on 2017/11/24.
 *
 * @since 1.0
 */
// FIXME: 2017/11/28 SharedPreferences 支持 Set<String>，此处尚未实现
public class PrefsHelper {

    private SharedPreferences mSharedPreferences;
    // SharedPreferences commit is sync;
    // apply is async. but, it maybe block anytime when activity pause or stop; (even cause ANR)
    // see: http://www.cloudchou.com/android/post-988.html
    // so we do commit at low priority, in sub threads;
    // visit mMemoryCache preferentially
    private final Bundle mMemoryCache = new Bundle();

    private final Context mContext;
    private final int mMode;
    private final String mFile;
    /**
     * @param file Desired preferences file
     */
    public PrefsHelper(String file) {
        this(file, Context.MODE_PRIVATE);
    }

    /**
     * @param file Desired preferences file
     * @param mode Desired preferences mode
     */
    public PrefsHelper(String file, int mode) {
        mContext = ARContext.app();
        mFile = file;
        mMode = mode;
    }

    /**
     * @param context context to get {@link SharedPreferences}
     * @param file    Desired preferences file
     */
    public PrefsHelper(Context context, String file) {
        this(context, file, Context.MODE_PRIVATE);
    }

    /**
     * @param context context to get {@link SharedPreferences}
     * @param file    Desired preferences file
     * @param mode    Desired preferences mode
     */
    public PrefsHelper(Context context, String file, int mode) {
        mContext = context.getApplicationContext();
        mFile = file;
        mMode = mode;
    }

    /**
     * @since 1.0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * @since 1.0
     */
    public int getInt(String key, int defaultValue) {
        sharedPreferences();
        synchronized (mMemoryCache) {
            return mMemoryCache.getInt(key, defaultValue);
        }
    }

    /**
     * @since 1.0
     */
    public long getLong(String key, long defaultValue) {
        sharedPreferences();
        synchronized (mMemoryCache) {
            return mMemoryCache.getLong(key, defaultValue);
        }
    }

    /**
     * @since 1.0
     */
    public float getFloat(String key, float defaultValue) {
        sharedPreferences();
        synchronized (mMemoryCache) {
            return mMemoryCache.getFloat(key, defaultValue);
        }
    }

    /**
     * @since 1.0
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * @since 1.0
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        sharedPreferences();
        synchronized (mMemoryCache) {
            return mMemoryCache.getBoolean(key, defaultValue);
        }
    }

    /**
     * @since 1.0
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * @since 1.0
     */
    public String getString(String key, String defaultValue) {
        sharedPreferences();
        synchronized (mMemoryCache) {
            String result = mMemoryCache.getString(key);
            if (null == result) {
                return mMemoryCache.containsKey(key) ? null : defaultValue;
            }
            return result;
        }
    }

    private SharedPreferences sharedPreferences() {
        if (mSharedPreferences == null) {
            synchronized (mMemoryCache) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = mContext.getSharedPreferences(mFile, mMode);
                    // load from raw SharedPreferences
                    onInit(mMemoryCache, mSharedPreferences);
                }
            }
        }
        return mSharedPreferences;
    }

    /**
     * @since 1.0.0
     */
    protected void onInit(Bundle bundle, SharedPreferences sharedPreferences) {
        Map<String, Object> d = new HashMap<>(sharedPreferences.getAll());
        if (!CollectionsCompat.isEmpty(d)) {
            for (Map.Entry<String, Object> entry: d.entrySet()) {
                final String key = entry.getKey();
                final Object val = entry.getValue();
                if (null == val) {
                    bundle.putString(key, null);
                } else {
                    if (val instanceof String) {
                        bundle.putString(key, (String) val);
                    } else if (val instanceof Integer) {
                        bundle.putInt(key, (Integer) val);
                    } else if (val instanceof Boolean) {
                        bundle.putBoolean(key, (Boolean) val);
                    } else if (val instanceof Long) {
                        bundle.putLong(key, (Long) val);
                    } else if (val instanceof Float) {
                        bundle.putFloat(key, (Float) val);
                    }
                }
            }
        }
    }

    /**
     * @since 1.0.0
     */
    public SharedPreferences.Editor edit() {
        return new SharedPreferences.Editor() {
            final SharedPreferences.Editor editor = sharedPreferences().edit();
            final Bundle data = new Bundle(mMemoryCache);

            @Override
            public SharedPreferences.Editor putString(String key, String value) {
                data.putString(key, value);
                editor.putString(key, value);
                return this;
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
//                synchronized (mMemoryCache) {
//                    data.putStringArrayList(key, values);
//                }
//                editor.putStringSet(key, values);
                return this;
            }

            @Override
            public SharedPreferences.Editor putInt(String key, int value) {
                data.putInt(key, value);
                editor.putInt(key, value);
                return this;
            }

            @Override
            public SharedPreferences.Editor putLong(String key, long value) {
                data.putLong(key, value);
                editor.putLong(key, value);
                return this;
            }

            @Override
            public SharedPreferences.Editor putFloat(String key, float value) {
                data.putFloat(key, value);
                editor.putFloat(key, value);
                return this;
            }

            @Override
            public SharedPreferences.Editor putBoolean(String key, boolean value) {
                data.putBoolean(key, value);
                editor.putBoolean(key, value);
                return this;
            }

            @Override
            public SharedPreferences.Editor remove(String key) {
                data.remove(key);
                editor.remove(key);
                return this;
            }

            @Override
            public SharedPreferences.Editor clear() {
                data.clear();
                editor.clear();
                return this;
            }

            @Override
            public boolean commit() {
                synchronized (mMemoryCache) {
                    mMemoryCache.clear();
                    mMemoryCache.putAll(data);
                }
                doInBackground(new Runnable() {
                    @Override
                    public void run() {
                        editor.commit();
                    }
                });
                return true;
            }

            @Override
            public void apply() {
                commit();
            }

            private void doInBackground(Runnable runnable) {
                Thread t = new Thread(runnable);
                try {
                    t.setPriority(Thread.MIN_PRIORITY);
                } catch (Throwable ignore) {}
                t.start();
            }
        };
    }

}
