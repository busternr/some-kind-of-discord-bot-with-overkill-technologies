import { init } from '@rematch/core';
import session from './session';

const store = init({
	models: {
		session
	}
});

export default store;
