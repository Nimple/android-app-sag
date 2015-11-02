package de.nimple.dagger;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.nimple.events.NoOpEvent;

public abstract class BaseFragment extends Fragment {
	@Inject
	EventBus eventBus;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((DaggerApplication) activity.getApplication()).inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		eventBus.register(this);
        setHasOptionsMenu(true);
		return inflater.inflate(getFragmentLayout(), container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.inject(this, view);
	}

	@Override
	public void onDestroyView() {
		eventBus.unregister(this);
		super.onDestroyView();
	}

	public void onEvent(NoOpEvent ev) {}

	/**
	 * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
	 * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
	 * inflate in this method when extends BaseFragment.
	 */
	protected abstract int getFragmentLayout();
}
