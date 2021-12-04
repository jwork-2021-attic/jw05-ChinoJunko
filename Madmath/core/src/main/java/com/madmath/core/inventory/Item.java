/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.inventory;

import com.badlogic.gdx.math.Vector2;

public interface Item {
    public boolean canPickUp(Vector2 position);

    public boolean use();
}
