/*
Copyright 2017 NickTheDev <http://nikdev.net/>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.nikdev.autobroadcast.event;

import net.nikdev.autobroadcast.broadcast.BroadCast;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import static net.nikdev.autobroadcast.util.Conditions.orElse;

public final class BroadCastEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final BroadCast broadcast;
    private boolean cancelled;

    public BroadCastEvent(BroadCast broadcast) {
        this.broadcast = orElse(broadcast, new BroadCast(null, null, null, null));
    }

    public BroadCast getBroadcast() {
        return broadcast;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
