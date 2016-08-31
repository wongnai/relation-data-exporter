package com.wongnai.tools.de.service.internal;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class KeyTest {
	@Test
	public void testSingleValue() {
		Key k1 = Key.createSingle(1);
		Key k2 = Key.createSingle(1);

		Assert.assertThat(k1, CoreMatchers.equalTo(k2));

		Assert.assertThat(k1.getVal(), CoreMatchers.equalTo(1));
	}

	@Test
	public void testSingleValueNotEqual() {
		Key k1 = Key.createSingle(1);
		Key k2 = Key.createSingle(2);

		Assert.assertThat(k1, CoreMatchers.not(CoreMatchers.equalTo(k2)));
	}

	@Test
	public void testComposite() {
		Key k1 = Key.createSingle(1);
		k1.add(2);
		Key k2 = Key.createSingle(1);
		k2.add(2);

		Assert.assertThat(k1, CoreMatchers.equalTo(k2));

		k1.add(3);

		Assert.assertThat(k1, CoreMatchers.not(CoreMatchers.equalTo(k2)));
	}

	@Test
	public void testRow() {
		Key k1 = Key.createAll(new Object[] {"a", "b" });
		Key k2 = Key.createAll(new Object[] {"a", "b" });

		Assert.assertThat(k1, CoreMatchers.equalTo(k2));
	}

	@Test
	public void testRowNotEqual() {
		Key k1 = Key.createAll(new Object[] {"a", "b" });
		Key k2 = Key.createAll(new Object[] {"a", "b", "c" });

		Assert.assertThat(k1, CoreMatchers.not(CoreMatchers.equalTo(k2)));
	}
}
